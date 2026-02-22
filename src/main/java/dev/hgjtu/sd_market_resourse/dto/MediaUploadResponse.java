package dev.hgjtu.sd_market_resourse.dto;

import java.util.UUID;

public class MediaUploadResponse {
    private UUID id;
    private String uploadUrl;

    public MediaUploadResponse(UUID id, String uploadUrl) {
        this.id = id;
        this.uploadUrl = uploadUrl;
    }

    public UUID getId() {
        return id;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }
}
