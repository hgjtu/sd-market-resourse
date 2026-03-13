package dev.hgjtu.sd_market_resourse.repos;

import dev.hgjtu.sd_market_resourse.models.Item;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface ItemRepository extends R2dbcRepository<Item, Long> {
    Flux<Item> findAllByCategoryIdAndTypeOrderByPublicationDate(Integer categoryId, String type);
    Flux<Item> findAllByUserIdAndTypeOrderByPublicationDate(Long userId, String type);
//    Flux<Item> findAllByUserIdOrderByPublicationDateDesc(Long userId);
}
