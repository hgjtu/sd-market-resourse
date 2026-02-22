package dev.hgjtu.sd_market_resourse.repos;

import dev.hgjtu.sd_market_resourse.models.ItemMedia;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ItemMediaRepository extends R2dbcRepository<ItemMedia, UUID> {
    Mono<ItemMedia> findFirstByItemIdAndSortOrder(Long itemId, Integer sortOrder);
    Flux<ItemMedia> findAllByItemIdOrderBySortOrderAsc(Long itemId);
}
