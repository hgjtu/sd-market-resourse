package dev.hgjtu.sd_market_resourse.dto;

import dev.hgjtu.sd_market_resourse.models.Comment;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.List;


public class ItemResponse {
    private String title;
    private String description;
    private String username;
    private List<String> imagesUrls;
    private Integer price;
    private String location;
    private LocalDate publicationDate;
    private List<Comment> comments;

    public ItemResponse(String title, String description, String username, List<String> imagesUrls, Integer price, String location, LocalDate publicationDate, List<Comment> comments) {
        this.title = title;
        this.description = description;
        this.username = username;
        this.imagesUrls = imagesUrls;
        this.price = price;
        this.location = location;
        this.publicationDate = publicationDate;
        this.comments = comments;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getImagesUrls() {
        return imagesUrls;
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
}
