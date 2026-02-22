package dev.hgjtu.sd_market_resourse.services;

import dev.hgjtu.sd_market_resourse.dto.MediaUploadResponse;
import dev.hgjtu.sd_market_resourse.models.Media;
import dev.hgjtu.sd_market_resourse.models.UserRole;
import dev.hgjtu.sd_market_resourse.repos.MediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

@Service
public class MediaService {
    private final S3Client s3Client;
    private final S3Presigner presigner;
    private final MediaRepository mediaRepository;

    @Value("${minio.bucket}")
    private String bucket;

    private final Random random = new Random();

    public MediaService(S3Client s3Client, S3Presigner presigner, MediaRepository mediaRepository) {
        this.s3Client = s3Client;
        this.presigner = presigner;
        this.mediaRepository = mediaRepository;
    }

    public Mono<MediaUploadResponse> generateUploadUrl(String username, String contentType) {
        String extension = switch (contentType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            default -> throw new IllegalArgumentException("Unsupported image type");
        };

        String fileName = username + "_profilePhoto_" + random.nextInt(1000, 10000) + extension;
        String objectKey = "profile_medias/" + fileName; // TODO тут будет совсем по-другому

        // создаём запись в БД со статусом PENDING
        Media media = new Media(
                null,
                fileName,
                contentType,
                objectKey,
                Media.Status.PENDING,
                Instant.now(),
                Instant.now()
        );
        return mediaRepository.save(media)
                .map(savedMedia -> {
                    // генерируем presigned PUT URL
                    PutObjectRequest objectRequest = PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(objectKey)
                            .contentType(contentType)
                            .build();

                    PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                            .signatureDuration(Duration.ofMinutes(10))
                            .putObjectRequest(objectRequest)
                            .build();

                    URL presignedUrl = presigner.presignPutObject(presignRequest).url();

                    return new MediaUploadResponse(savedMedia.getId(), presignedUrl.toString());
                });
    }

    public Mono<Media> completeUpload(UUID mediaId) {
        return mediaRepository.findById(mediaId)
                .switchIfEmpty(Mono.error(new RuntimeException("Media not found")))
                .flatMap(
                        media -> {
                            media.setStatus(Media.Status.READY);
                            media.setUpdatedAt(Instant.now());

                            return mediaRepository.save(media);
                        }
                );
    }

    public Mono<URL> generateDownloadUrl(UUID mediaId) {
        return Mono.justOrEmpty(mediaId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("mediaId must not be null")))
                .flatMap(mediaRepository::findById)
                .switchIfEmpty(Mono.error(new RuntimeException("Media not found")))
                .flatMap(media -> {
                    if (media.getStatus() != Media.Status.READY) {
                        return Mono.error(new RuntimeException("Media not ready for download"));
                    }
                    return Mono.just(presigner.presignGetObject(builder -> builder
                            .getObjectRequest(req -> req.bucket(bucket).key(media.getObjectKey()))
                            .signatureDuration(Duration.ofMinutes(10))
                    ).url());
                });
    }

    public Mono<Void> deleteMedia(UUID mediaId, String username, UserRole userRole) {
        return mediaRepository.findById(mediaId)
                .switchIfEmpty(Mono.error(new RuntimeException("Media not found")))
                .flatMap(media -> {
                    // только владелец или админ)
                    if (!media.getFileName().startsWith(username + "_profilePhoto_") &&
                            userRole != UserRole.ROLE_ADMIN) {
                        return Mono.error(new RuntimeException("No permission to delete this media"));
                    }

                    return Mono.fromRunnable(() ->
                                    s3Client.deleteObject(builder -> builder
                                            .bucket(bucket)
                                            .key(media.getObjectKey())
                                            .build())
                            ).subscribeOn(Schedulers.boundedElastic())
                            .then(mediaRepository.delete(media));
                });
    }
}
