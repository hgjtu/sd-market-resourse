package dev.hgjtu.sd_market_resourse.controllers;

import dev.hgjtu.sd_market_resourse.models.Category;
import dev.hgjtu.sd_market_resourse.repos.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

//    @GetMapping("/{id}")
//    public Mono<Category> getCategoryInfo(@PathVariable Integer id) {
//        return categoryRepository.findById(id);
//    }

    @GetMapping("/{name}")
    public Mono<Category> getCategoryInfo(@PathVariable String name) {
        return categoryRepository.findByName(name);
    }

    @GetMapping
    public Flux<Category> getAllCategories() {
        return categoryRepository.findAllByOrderByIdAsc();
    }
}
