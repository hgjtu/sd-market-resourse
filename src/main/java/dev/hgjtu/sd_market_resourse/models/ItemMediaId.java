package dev.hgjtu.sd_market_resourse.models;

import java.io.Serializable;
import java.util.UUID;

public class ItemMediaId implements Serializable {
    private Long itemId;
    private UUID mediaId;

    public ItemMediaId() {
    }

    public ItemMediaId(Long itemId, UUID mediaId) {
        this.itemId = itemId;
        this.mediaId = mediaId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public UUID getMediaId() {
        return mediaId;
    }

    public void setMediaId(UUID mediaId) {
        this.mediaId = mediaId;
    }
}
