package dev.hgjtu.sd_market_resourse.services;

import dev.hgjtu.sd_market_resourse.dto.ItemMinResponse;
import dev.hgjtu.sd_market_resourse.dto.ItemResponse;
import dev.hgjtu.sd_market_resourse.repos.CategoryRepository;
import dev.hgjtu.sd_market_resourse.repos.ItemRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    private final WebClient webClient;
    @Value("${USER_RESOURCE_SERVER_URL}")
    private String resourceAUrl;

    public ItemService(ItemRepository itemRepository, CategoryRepository categoryRepository, WebClient webClient) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.webClient = webClient;
    }

//    public Mono<String> getCombinedData() {
//        return webClient.get()
//                .uri(resourceAUrl + "/api/users/all-by-ids")
//                .retrieve()
//                .bodyToMono(String.class); // возвращаем ответ как есть
//    }

    public Flux<ItemMinResponse> getAllByCategoryAndType(String category, String type) {
        return categoryRepository.existsByName(category)
                .flatMapMany(exists -> {
                    if (exists) {
                        return categoryRepository.findByName(category)
                                .flatMapMany(category1 ->
                                        itemRepository.findAllByCategoryIdAndType(category1.getId(), type)
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

    public Mono<ItemResponse> addItem() {
        return Mono.empty();
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
