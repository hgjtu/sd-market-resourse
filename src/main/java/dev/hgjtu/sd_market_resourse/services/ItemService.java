package dev.hgjtu.sd_market_resourse.services;

import dev.hgjtu.sd_market_resourse.dto.ItemMinResponse;
import dev.hgjtu.sd_market_resourse.repos.CategoryRepository;
import dev.hgjtu.sd_market_resourse.repos.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    private final WebClient webClient;
    @Value("${USER_RESOURCE_SERVER_URL}")
    private String resourceAUrl;

//    public Mono<String> getCombinedData() {
//        return webClient.get()
//                .uri(resourceAUrl + "/api/users/all-by-ids")
//                .retrieve()
//                .bodyToMono(String.class); // возвращаем ответ как есть
//    }

    public Flux<ItemMinResponse> getAllByCategory(String category) {
        return categoryRepository.existsByName(category)
                .flatMapMany(exists -> {
                    if (exists) {
                        return categoryRepository.findByName(category)
                                .flatMapMany(category1 ->
                                        itemRepository.findAllByCategory(category1)
                                                .map(item -> new ItemMinResponse(
                                                        item.getId(),
                                                        item.getTitle(),
                                                        item.getImagesUrls().get(0),
                                                        item.getPrice()
                                                ))
                                );
                    } else {
                        return Flux.error(new Exception("Category not found"));
                    }
                });
    }

    public Flux<ItemMinResponse> getAllByCategoryId(Integer id) {
        return categoryRepository.existsById(id)
                .flatMapMany(exists -> {
                    if (exists) {
                        return categoryRepository.findById(id)
                                .flatMapMany(category1 ->
                                        itemRepository.findAllByCategory(category1)
                                                .map(item -> new ItemMinResponse(
                                                        item.getId(),
                                                        item.getTitle(),
                                                        item.getImagesUrls().get(0),
                                                        item.getPrice()
                                                ))
                                );
                    } else {
                        return Flux.error(new Exception("Category not found"));
                    }
                });
    }

}
