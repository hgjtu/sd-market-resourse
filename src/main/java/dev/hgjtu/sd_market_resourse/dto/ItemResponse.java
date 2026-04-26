package dev.hgjtu.sd_market_resourse.dto;

import dev.hgjtu.sd_market_resourse.models.Comment;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.List;


public class ItemResponse {
    private Long id;
    private Integer categoryId;
    private String title;
    private String description;
    private UserWithMediaForResources authorInfo;
    private List<MediaUploadResponse> medias;
    private Integer price;
    private String location;
    private LocalDate publicationDate;
    private String type;
    private List<Comment> comments;

    public ItemResponse(Long id, Integer categoryId, String title, String description, UserWithMediaForResources authorInfo, List<MediaUploadResponse> medias, Integer price, String location, LocalDate publicationDate, String type, List<Comment> comments) {
        this.id = id;
        this.categoryId = categoryId;
        this.title = title;
        this.description = description;
        this.authorInfo = authorInfo;
        this.medias = medias;
        this.price = price;
        this.location = location;
        this.publicationDate = publicationDate;
        this.type = type;
        this.comments = comments;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<MediaUploadResponse> getMedias() {
        return medias;
    }

    public Integer getPrice() {
        return price;
    }

    public String getLocation() {
        return location;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public String getType() {
        return type;
    }

    public UserWithMediaForResources getAuthorInfo() {
        return authorInfo;
    }
}
