package dev.hgjtu.sd_market_resourse.controllers;

import dev.hgjtu.sd_market_resourse.dto.MediaUploadResponse;
import dev.hgjtu.sd_market_resourse.dto.UploadUrlRequest;
import dev.hgjtu.sd_market_resourse.models.Media;
import dev.hgjtu.sd_market_resourse.services.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URL;
import java.util.UUID;

@RestController
@RequestMapping("/api/users-resource/media")
public class MediaController {
    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @PostMapping("/upload-url")
    public Mono<MediaUploadResponse> getUploadUrl(@RequestBody UploadUrlRequest request,
                                                  @AuthenticationPrincipal Jwt jwt) {
        return mediaService.generateUploadUrl(
                jwt.getClaim("sub"),
                request.getContentType()
        );
    }

    @PostMapping("/complete/{id}")
    public Mono<Media> completeUpload(@PathVariable UUID id) {
         return mediaService.completeUpload(id);
    }

    @GetMapping("/download-url/{id}")
    public Mono<URL> getDownloadUrl(@PathVariable UUID id) {
        return mediaService.generateDownloadUrl(id);
    }
}
