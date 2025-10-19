package dev.hgjtu.sd_market_resourse.repos;

import dev.hgjtu.sd_market_resourse.models.Comment;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface CommentRepository extends R2dbcRepository<Comment, Long>  {
    Flux<Comment> findAllByItemId(Long itemId);
}
