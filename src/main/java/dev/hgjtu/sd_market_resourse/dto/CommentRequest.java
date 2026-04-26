package dev.hgjtu.sd_market_resourse.dto;

public class CommentRequest {
    private Long itemId;
    private Long replyCommentId;
    private String content;

    public CommentRequest(Long itemId, Long replyCommentId, String content) {
        this.itemId = itemId;
        this.replyCommentId = replyCommentId;
        this.content = content;
    }

    public Long getItemId() {
        return itemId;
    }

    public Long getReplyCommentId() {
        return replyCommentId;
    }

    public String getContent() {
        return content;
    }
}
