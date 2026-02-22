package dev.hgjtu.sd_market_resourse.services;

import dev.hgjtu.sd_market_resourse.dto.ItemMinResponse;
import dev.hgjtu.sd_market_resourse.dto.ItemRequest;
import dev.hgjtu.sd_market_resourse.dto.ItemResponse;
import dev.hgjtu.sd_market_resourse.feignClients.UserClient;
import dev.hgjtu.sd_market_resourse.models.Comment;
import dev.hgjtu.sd_market_resourse.models.Item;
import dev.hgjtu.sd_market_resourse.models.ItemMedia;
import dev.hgjtu.sd_market_resourse.repos.CategoryRepository;
import dev.hgjtu.sd_market_resourse.repos.CommentRepository;
import dev.hgjtu.sd_market_resourse.repos.ItemMediaRepository;
import dev.hgjtu.sd_market_resourse.repos.ItemRepository;
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

    private final UserClient userClient;

    private final MediaService mediaService;

    @Autowired
    public ItemService(ItemRepository itemRepository, ItemMediaRepository itemMediaRepository,
                       CategoryRepository categoryRepository, CommentRepository commentRepository, UserClient userClient, MediaService mediaService) {
        this.itemRepository = itemRepository;
        this.itemMediaRepository = itemMediaRepository;
        this.categoryRepository = categoryRepository;
        this.commentRepository = commentRepository;
        this.userClient = userClient;
        this.mediaService = mediaService;
    }

    public Flux<ItemMinResponse> getAllByCategoryAndType(String category, String type) {
        return categoryRepository.existsByName(category)
                .flatMapMany(exists -> {
                    if (exists) {
                        return categoryRepository.findByName(category)
                                .flatMapMany(category1 ->
                                        itemRepository.findAllByCategoryIdAndTypeOrderByPublicationDate(category1.getId(), type)
                                                .flatMap(item ->
                                                        itemMediaRepository.findFirstByItemIdAndSortOrder(item.getId(), 0)
                                                                .flatMap(itemMedia ->
                                                                        mediaService.generateDownloadUrl(itemMedia.getMediaId())
                                                                                .map(URL::toString)
                                                                                .defaultIfEmpty("")
                                                                                .onErrorResume(e -> Mono.just(""))
                                                                )
                                                                .map(url -> new ItemMinResponse(
                                                                        item.getId(),
                                                                        item.getTitle(),
                                                                        url.isEmpty() ? null : url,
                                                                        item.getPrice()
                                                                ))
                                                )
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
                                .flatMap(item ->
                                        itemMediaRepository.findFirstByItemIdAndSortOrder(item.getId(), 0)
                                                .flatMap(itemMedia ->
                                                        mediaService.generateDownloadUrl(itemMedia.getMediaId())
                                                                .map(URL::toString)
                                                                .defaultIfEmpty("")
                                                                .onErrorResume(e -> Mono.just(""))
                                                )
                                                .map(url -> new ItemMinResponse(
                                                        item.getId(),
                                                        item.getTitle(),
                                                        url.isEmpty() ? null : url,
                                                        item.getPrice()
                                                ))
                                );
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
                                                        .map(URL::toString)
                                                        .defaultIfEmpty("")
                                                        .onErrorResume(e -> Mono.just(""))
                                                )
                                                .collectList()
                                                .flatMap(urls ->
                                                        commentRepository.findAllByItemId(item.getId())
                                                                .collectList()
                                                                .map(comments -> mapToItemResponse(item, username, comments, urls))
                                                )
                                )

                );
    }

    public Mono<ItemResponse> addItem(String username, ItemRequest itemRequest) {
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
                        )).map(newItem -> mapToItemResponse(newItem, username, List.of(), List.of())); // TODO а вот тут хз, как доавблять медиа при создании
                    }
                });
    }

    public Mono<ItemResponse> editItem(Long itemId, String username, ItemRequest itemRequest) {
        return checkUserExistenceByUsername(username)
                .flatMap(userId -> {
                    if (userId == -1L) {
                        return Mono.error(new RuntimeException("User not found"));
                    } else {
                        return itemRepository.findById(itemId)
                                .switchIfEmpty(Mono.error(new RuntimeException("Item not found")))
                                .flatMap(item -> {
                                    if(Objects.equals(item.getUserId(), userId)){
                                        item.setTitle(itemRequest.getTitle());
                                        item.setCategoryId(itemRequest.getCategoryId());
                                        item.setDescription(itemRequest.getDescription());
                                        item.setPrice(itemRequest.getPrice());
                                        item.setLocation(itemRequest.getLocation());
                                        item.setType(itemRequest.getType());

                                        return itemRepository.save(item);
                                    }
                                    else{
                                        return Mono.error(new RuntimeException("Item not found"));
                                    }
                                })
                                .flatMap(savedItem ->
                                        itemMediaRepository.findAllByItemIdOrderBySortOrderAsc(savedItem.getId())
                                                .flatMap(itemMedia -> mediaService.generateDownloadUrl(itemMedia.getMediaId())
                                                        .map(URL::toString)
                                                        .defaultIfEmpty("")
                                                        .onErrorResume(e -> Mono.just(""))
                                                )
                                                .collectList()
                                                .flatMap(urls ->
                                                        commentRepository.findAllByItemId(savedItem.getId())
                                                                .collectList()
                                                                .map(comments -> mapToItemResponse(savedItem, username, comments, urls))
                                                )
                                );
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
                                .then(Mono.just("Success"));
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

    private ItemResponse mapToItemResponse(Item item, String username, List<Comment> comments, List<String> mediaURLs) {
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
