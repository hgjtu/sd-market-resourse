package dev.hgjtu.sd_market_resourse.dto;

public class UploadUrlRequest {
    private String fileName;
    private String contentType;

    public UploadUrlRequest() {
    }

    public UploadUrlRequest(String fileName, String contentType) {
        this.fileName = fileName;
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}


