package dev.hgjtu.sd_market_resourse.controllers;

import dev.hgjtu.sd_market_resourse.dto.CategoryResponse;
import dev.hgjtu.sd_market_resourse.dto.ItemMinResponse;
import dev.hgjtu.sd_market_resourse.models.Category;
import dev.hgjtu.sd_market_resourse.repos.CategoryRepository;
import dev.hgjtu.sd_market_resourse.services.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URL;

@RestController
@RequestMapping("/api/market-resource/categories")
public class CategoryController {
    private final CategoryRepository categoryRepository;
    private final MediaService mediaService;

    @Autowired
    public CategoryController(CategoryRepository categoryRepository, MediaService mediaService) {
        this.categoryRepository = categoryRepository;
        this.mediaService = mediaService;
    }

//    @GetMapping("/{id}")
//    public Mono<Category> getCategoryInfo(@PathVariable Integer id) {
//        return categoryRepository.findById(id);
//    }

    @GetMapping("/{name}")
    public Mono<CategoryResponse> getCategoryInfo(@PathVariable String name) {
        return categoryRepository.findByName(name)
                .flatMap(category -> mediaService.generateDownloadUrl(category.getCategoryMedia())
                        .map(URL::toString)
                        .defaultIfEmpty("")
                        .onErrorResume(e -> Mono.just(""))
                        .map(url -> new CategoryResponse(
                                category,
                                url.isEmpty() ? null : url
                        ))
                );
    }

    @GetMapping
    public Flux<CategoryResponse> getAllCategories() {
        return categoryRepository.findAllByOrderByIdAsc()
                .flatMap(category -> mediaService.generateDownloadUrl(category.getCategoryMedia())
                        .map(URL::toString)
                        .defaultIfEmpty("")
                        .onErrorResume(e -> Mono.just(""))
                        .map(url -> new CategoryResponse(
                                category,
                                url.isEmpty() ? null : url
                        ))
                );
    }
}
