package dev.hgjtu.sd_market_resourse.dto;

import java.time.LocalDateTime;

public class CommentResponse {
    private Long id;
    private Long itemId;
    private String userId;
    private Long replyCommentId;
    private String content;
    private Integer likes;
    private LocalDateTime createdAt;

    public CommentResponse() {
    }

    public CommentResponse(Long id, Long itemId, String userId, Long replyCommentId, String content, Integer likes, LocalDateTime createdAt) {
        this.id = id;
        this.itemId = itemId;
        this.userId = userId;
        this.replyCommentId = replyCommentId;
        this.content = content;
        this.likes = likes;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getItemId() {
        return itemId;
    }

    public String getUserId() {
        return userId;
    }

    public Long getReplyCommentId() {
        return replyCommentId;
    }

    public String getContent() {
        return content;
    }

    public Integer getLikes() {
        return likes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
