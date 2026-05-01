package dev.hgjtu.sd_market_resourse.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("comments")
public class Comment {
    @Id
    private Long id;

    @Column("item_id")
    private Long itemId;

    @Column("user_id")
    private Long userId;

    @Column("author_username")
    private String authorUsername;

    @Column("reply_comment_id")
    private Long replyCommentId;

    @Column("content")
    private String content;

    @Column("likes")
    private Integer likes;

    @Column("created_at")
    private LocalDateTime createdAt;

    public Comment() {
    }

    public Comment(Long itemId, Long userId, String authorUsername, Long replyCommentId, String content, Integer likes, LocalDateTime createdAt) {
        this.itemId = itemId;
        this.userId = userId;
        this.authorUsername = authorUsername;
        this.replyCommentId = replyCommentId;
        this.content = content;
        this.likes = likes;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getReplyCommentId() {
        return replyCommentId;
    }

    public void setReplyCommentId(Long replyCommentId) {
        this.replyCommentId = replyCommentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }
}
