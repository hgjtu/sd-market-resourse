package dev.hgjtu.sd_market_resourse.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Table("comments")
public class Comment {
    @Id
    private Long id;

    @Column("item_id")
    private Long itemId;

    @Column("user_id")
    private Long userId;

    @Column("reply_comment_id")
    private Long replyCommentId;

    @Column("content")
    private String content;

}
