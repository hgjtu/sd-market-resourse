package dev.hgjtu.sd_market_resourse;

import dev.hgjtu.sd_market_resourse.controllers.CategoryController;
import dev.hgjtu.sd_market_resourse.models.Category;
import dev.hgjtu.sd_market_resourse.repos.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    private CategoryRepository categoryRepository;
    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        categoryRepository = mock(CategoryRepository.class);
        categoryController = new CategoryController(categoryRepository);
    }

    @Test
    void testGetCategoryInfoByName() {
        Category category = new Category();
        category.setId(1);
        category.setName("altimeter");
        category.setNameRu("Высотомеры");
        category.setDescription("Большое описание 1");
        category.setShortDescription("Короткое описание 1");

        when(categoryRepository.findByName("altimeter")).thenReturn(Mono.just(category));

        Mono<Category> result = categoryController.getCategoryInfo("altimeter");

        StepVerifier.create(result)
                .expectNextMatches(c -> c.getName().equals("altimeter") && c.getId() == 1)
                .verifyComplete();

        verify(categoryRepository, times(1)).findByName("altimeter");
    }

    @Test
    void testGetAllCategories() {
        Category cat1 = new Category();
        cat1.setId(1);
        cat1.setName("altimeter");

        Category cat2 = new Category();
        cat2.setId(2);
        cat2.setName("helmet");

        when(categoryRepository.findAllByOrderByIdAsc()).thenReturn(Flux.fromIterable(Arrays.asList(cat1, cat2)));

        Flux<Category> result = categoryController.getAllCategories();

        StepVerifier.create(result)
                .expectNext(cat1)
                .expectNext(cat2)
                .verifyComplete();

        verify(categoryRepository, times(1)).findAllByOrderByIdAsc();
    }

    @Test
    void testGetCategoryInfoByNameNotFound() {
        when(categoryRepository.findByName(anyString())).thenReturn(Mono.empty());

        Mono<Category> result = categoryController.getCategoryInfo("nonexistent");

        StepVerifier.create(result)
                .verifyComplete();

        verify(categoryRepository, times(1)).findByName("nonexistent");
    }
}
