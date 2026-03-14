package dev.hgjtu.sd_market_resourse.services;

import dev.hgjtu.sd_market_resourse.dto.ItemMinResponse;
import dev.hgjtu.sd_market_resourse.dto.ItemRequest;
import dev.hgjtu.sd_market_resourse.dto.ItemResponse;
import dev.hgjtu.sd_market_resourse.dto.MediaUploadResponse;
import dev.hgjtu.sd_market_resourse.feignClients.UserClient;
import dev.hgjtu.sd_market_resourse.models.*;
import dev.hgjtu.sd_market_resourse.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemMediaRepository itemMediaRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;
    private final MediaRepository mediaRepository;

    private final UserClient userClient;

    private final MediaService mediaService;

    @Autowired
    public ItemService(ItemRepository itemRepository, ItemMediaRepository itemMediaRepository,
                       CategoryRepository categoryRepository, CommentRepository commentRepository, UserClient userClient, MediaService mediaService, MediaRepository mediaRepository) {
        this.itemRepository = itemRepository;
        this.itemMediaRepository = itemMediaRepository;
        this.categoryRepository = categoryRepository;
        this.commentRepository = commentRepository;
        this.userClient = userClient;
        this.mediaService = mediaService;
        this.mediaRepository = mediaRepository;
    }

    public Flux<ItemMinResponse> getAllByCategoryAndType(String category, String type) {
        return categoryRepository.existsByName(category)
                .flatMapMany(exists -> {
                    if (exists) {
                        return categoryRepository.findByName(category)
                                .flatMapMany(category1 ->
                                        itemRepository.findAllByCategoryIdAndTypeOrderByPublicationDate(category1.getId(), type)
                                                .flatMap(this::buildItemMinResponse)
                                );
                    } else {
                        return Flux.error(new Exception("Category not found"));
                    }
                });
    }

    public Flux<ItemMinResponse> getAllByUsernameAndType(String username, String type) {
        return checkUserExistenceByUsername(username)
                .flatMapMany(userId -> {
                    if (userId == -1L) {
                        return Mono.error(new RuntimeException("User not found"));
                    } else {
                        return itemRepository.findAllByUserIdAndTypeOrderByPublicationDate(userId, type)
                                .flatMap(this::buildItemMinResponse);
                    }
                });
    }

    public Mono<ItemResponse> getItemById(Long id) {
        return itemRepository.findById(id)
                .flatMap(item ->
                        getUsernameById(item.getUserId())
                                .flatMap(username ->
                                        itemMediaRepository.findAllByItemIdOrderBySortOrderAsc(item.getId())
                                                .flatMap(itemMedia -> mediaService.generateDownloadUrl(itemMedia.getMediaId())
                                                        .map(url -> new MediaUploadResponse(itemMedia.getMediaId(), url.toString()))
                                                        .defaultIfEmpty(new MediaUploadResponse(itemMedia.getMediaId(), ""))
                                                        .onErrorResume(e -> Mono.just(new MediaUploadResponse(itemMedia.getMediaId(), "")))
                                                )
                                                .collectList()
                                                .flatMap(medias ->
                                                        commentRepository.findAllByItemId(item.getId())
                                                                .collectList()
                                                                .map(comments -> mapToItemResponse(item, username, comments, medias))
                                                )
                                )

                );
    }

    public Mono<Long> addItem(String username, ItemRequest itemRequest) {
        return checkUserExistenceByUsername(username)
                .flatMap(userId -> {
                    if (userId == -1L) {
                        return Mono.error(new RuntimeException("User not found"));
                    } else {
                        return itemRepository.save(new Item(
                                itemRequest.getCategoryId(),
                                userId,
                                itemRequest.getTitle(),
                                itemRequest.getDescription(),
                                itemRequest.getPrice(),
                                itemRequest.getLocation(),
                                itemRequest.getType(),
                                LocalDate.now()
                        )).map(Item::getId);
                    }
                });
    }

    public Mono<Long> editItem(Long itemId, String username, ItemRequest itemRequest) {
        return checkUserExistenceByUsername(username)
                .flatMap(userId -> {
                    if (userId == -1L) {
                        return Mono.error(new RuntimeException("User not found"));
                    } else {
                        return itemRepository.findById(itemId)
                                .switchIfEmpty(Mono.error(new RuntimeException("Item not found")))
                                .flatMap(item -> {
                                    if(Objects.equals(item.getUserId(), userId)){
                                        Optional.ofNullable(itemRequest.getTitle()).ifPresent(item::setTitle);
                                        Optional.ofNullable(itemRequest.getCategoryId()).ifPresent(item::setCategoryId);
                                        Optional.ofNullable(itemRequest.getDescription()).ifPresent(item::setDescription);
                                        Optional.ofNullable(itemRequest.getPrice()).ifPresent(item::setPrice);
                                        Optional.ofNullable(itemRequest.getLocation()).ifPresent(item::setLocation);
                                        Optional.ofNullable(itemRequest.getType()).ifPresent(item::setType);

                                        return itemRepository.save(item)
                                                .map(Item::getId);
                                    }
                                    else{
                                        return Mono.error(new RuntimeException("Item not found"));
                                    }
                                });
//                                .flatMap(savedItem ->
//                                        itemMediaRepository.findAllByItemIdOrderBySortOrderAsc(savedItem.getId())
//                                                .flatMap(itemMedia -> mediaService.generateDownloadUrl(itemMedia.getMediaId())
//                                                        .map(URL::toString)
//                                                        .defaultIfEmpty("")
//                                                        .onErrorResume(e -> Mono.just(""))
//                                                )
//                                                .collectList()
//                                                .flatMap(urls ->
//                                                        commentRepository.findAllByItemId(savedItem.getId())
//                                                                .collectList()
//                                                                .map(comments -> mapToItemResponse(savedItem, username, comments, urls))
//                                                )
//                                );
                    }
                });
    }

    public Mono<Void> addItemMedia(Long itemId, String username, List<UUID> mediaList) {
        return checkUserExistenceByUsername(username)
                .flatMapMany(userId -> {
                    if (userId == -1L) {
                        return Mono.error(new RuntimeException("User not found"));
                    } else {
                        return Flux.fromIterable(mediaList)
                                .flatMap(mediaId -> mediaRepository.findById(mediaId)
                                            .switchIfEmpty(Mono.error(new RuntimeException("Media not found: " + mediaId)))
                                            .flatMap(media -> {
                                                if (media.getStatus() != Media.Status.READY) {
                                                    return Mono.error(new RuntimeException("Media not ready: " + mediaId));
                                                }

                                                return itemMediaRepository.save(new ItemMedia(itemId, mediaId, 0))
                                                        .then(Mono.empty());
//                                                        .flatMap(savedItemMedia ->
//                                                                mediaService.generateDownloadUrl(savedItemMedia.getMediaId())
//                                                                        .map(URL::toString)
//                                                                        .defaultIfEmpty("")
//                                                                        .onErrorResume(e -> Mono.just(""))
//                                                        );
                                            })
                                            , 5);
                    }
                }).then();
    }

    public Mono<Void> deleteItemMedia(Long itemId, String username, UUID mediaId) {
        return checkUserExistenceByUsername(username)
                .flatMap(userId -> {
                    if (userId == -1L) {
                        return Mono.error(new RuntimeException("User not found"));
                    } else {
                        return itemRepository.findById(itemId)
                                .switchIfEmpty(Mono.error(new RuntimeException("Item not found")))
                                .flatMap(item -> {
                                    if(Objects.equals(item.getUserId(), userId)){
                                        return mediaService.deleteMedia(mediaId, username, UserRole.ROLE_USER)
                                                .then(Mono.defer(() -> itemMediaRepository.deleteByItemIdAndMediaId(itemId, mediaId)));
                                    }
                                    else{
                                        return Mono.error(new RuntimeException("Item not found"));
                                    }
                                });
                    }
                });
    }

    public Mono<String> deleteItem(Long itemId, String username) {
        return checkUserExistenceByUsername(username)
                .flatMap(userId -> {
                    if (userId == -1L) {
                        return Mono.error(new RuntimeException("User not found"));
                    } else {
                        return itemRepository.findById(itemId)
                                .switchIfEmpty(Mono.error(new RuntimeException("Item not found")))
                                .flatMap(item -> {
                                    if(Objects.equals(item.getUserId(), userId)){
                                        return itemRepository.delete(item);
                                    }
                                    else{
                                        return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
                                    }
                                })
                                .then(Mono.just("Success")); // TODO удалять надо все связанные медиа
                    }
                });
    }

    private Mono<Long> checkUserExistenceByUsername(String username) {
        return userClient.checkUserExistenceByUsername(username)
                .switchIfEmpty(Mono.just(-1L));
    }

    private Mono<String> getUsernameById(Long id) {
        return userClient.getUsernameById(id)
                .switchIfEmpty(Mono.empty());
    }

    private ItemResponse mapToItemResponse(Item item, String username, List<Comment> comments, List<MediaUploadResponse> mediaURLs) {
        return new ItemResponse(
                item.getId(),
                item.getCategoryId(),
                item.getTitle(),
                item.getDescription(),
                username,
                mediaURLs,
                item.getPrice(),
                item.getLocation(),
                item.getPublicationDate(),
                item.getType(),
                comments
        );
    }

    private Mono<ItemMinResponse> buildItemMinResponse(Item item) {
        return itemMediaRepository.findFirstByItemIdAndSortOrder(item.getId(), 0)
                .flatMap(itemMedia ->
                        mediaService.generateDownloadUrl(itemMedia.getMediaId())
                                .map(URL::toString)
                                .map(url -> new ItemMinResponse(
                                        item.getId(),
                                        item.getTitle(),
                                        url,
                                        item.getPrice()
                                ))
                                .onErrorResume(e -> Mono.just(new ItemMinResponse(
                                        item.getId(),
                                        item.getTitle(),
                                        null,
                                        item.getPrice()
                                )))
                )
                .switchIfEmpty(Mono.just(new ItemMinResponse(
                        item.getId(),
                        item.getTitle(),
                        null,
                        item.getPrice()
                )));
    }

//    public Flux<ItemMinResponse> getAllByCategoryId(Integer id) {
//        return categoryRepository.existsById(id)
//                .flatMapMany(exists -> {
//                    if (exists) {
//                        return itemRepository.findAllByCategoryId(id)
//                                .map(item -> new ItemMinResponse(
//                                        item.getId(),
//                                        item.getTitle(),
//                                        Optional.ofNullable(item.getImagesUrls())
//                                                .filter(list -> !list.isEmpty())
//                                                .map(list -> list.get(0))
//                                                .orElse(null),
//                                        item.getPrice()
//                                ));
//                    } else {
//                        return Flux.error(new Exception("Category not found"));
//                    }
//                });
//    }
}
