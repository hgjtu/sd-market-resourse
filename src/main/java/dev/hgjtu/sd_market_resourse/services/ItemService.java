package dev.hgjtu.sd_market_resourse.services;

import dev.hgjtu.sd_market_resourse.dto.ItemMinResponse;
import dev.hgjtu.sd_market_resourse.dto.ItemRequest;
import dev.hgjtu.sd_market_resourse.dto.ItemResponse;
import dev.hgjtu.sd_market_resourse.models.Comment;
import dev.hgjtu.sd_market_resourse.models.Item;
import dev.hgjtu.sd_market_resourse.repos.CategoryRepository;
import dev.hgjtu.sd_market_resourse.repos.CommentRepository;
import dev.hgjtu.sd_market_resourse.repos.ItemRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;

    private final WebClient webClient;
    @Value("${USER_RESOURCE_SERVER_URL}")
    private String userResourceUrl;

    public ItemService(ItemRepository itemRepository, CategoryRepository categoryRepository, CommentRepository commentRepository, WebClient webClient) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.commentRepository = commentRepository;
        this.webClient = webClient;
    }

    public Flux<ItemMinResponse> getAllByCategoryAndType(String category, String type) {
        return categoryRepository.existsByName(category)
                .flatMapMany(exists -> {
                    if (exists) {
                        return categoryRepository.findByName(category)
                                .flatMapMany(category1 ->
                                        itemRepository.findAllByCategoryIdAndTypeOrderByPublicationDate(category1.getId(), type)
                                                .map(item -> new ItemMinResponse(
                                                        item.getId(),
                                                        item.getTitle(),
                                                        Optional.ofNullable(item.getImagesUrls())
                                                                .filter(list -> !list.isEmpty())
                                                                .map(list -> list.get(0))
                                                                .orElse(null),
                                                        item.getPrice()
                                                ))
                                );
                    } else {
                        return Flux.error(new Exception("Category not found"));
                    }
                });
    }

    public Mono<ItemResponse> getItemById(Long id) {
        return itemRepository.findById(id)
                .flatMap(item ->
                        getUsernameById(item.getUserId())
                                .flatMap(username ->
                                        commentRepository.findAllByItemId(id)
                                                .collectList()
                                                .map(comments -> mapToItemResponse(item, username, comments))
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
                                itemRequest.getImagesUrls(),
                                itemRequest.getPrice(),
                                itemRequest.getLocation(),
                                itemRequest.getType(),
                                LocalDate.now()
                        )).map(newItem -> mapToItemResponse(newItem, username, List.of()));
                    }
                });
    }

    public Mono<Long> checkUserExistenceByUsername(String username) {
        return webClient.get()
                .uri(userResourceUrl + "/api/users/exists-by-username/{username}", username)
                .retrieve()
                .bodyToMono(Long.class)
                .switchIfEmpty(Mono.just(-1L));
    }

    public Mono<String> getUsernameById(Long id) {
        return webClient.get()
                .uri(userResourceUrl + "/api/users/get-username/{id}", id)
                .retrieve()
                .bodyToMono(String.class)
                .switchIfEmpty(Mono.empty());
    }

    private ItemResponse mapToItemResponse(Item item, String username, List<Comment> comments) {
        return new ItemResponse(
                item.getTitle(),
                item.getDescription(),
                username,
                item.getImagesUrls(),
                item.getPrice(),
                item.getLocation(),
                item.getPublicationDate(),
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
