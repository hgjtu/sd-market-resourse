package dev.hgjtu.sd_market_resourse.repos;

import dev.hgjtu.sd_market_resourse.models.Category;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CategoryRepository extends R2dbcRepository<Category, Integer> {
    Mono<Boolean> existsByName(String name);
    Mono<Category> findByName(String name);
    Flux<Category> findAllByOrderByIdAsc();
}
