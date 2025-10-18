package dev.hgjtu.sd_market_resourse.controllers;

import dev.hgjtu.sd_market_resourse.models.Category;
import dev.hgjtu.sd_market_resourse.repos.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryRepository categoryRepository;

    @GetMapping("/{id}")
    public Mono<Category> getCategoryInfo(@PathVariable Integer id) {
        return categoryRepository.findById(id);
    }

    @GetMapping
    public Flux<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
