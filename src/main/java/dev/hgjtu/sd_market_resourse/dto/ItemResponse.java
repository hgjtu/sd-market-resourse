package dev.hgjtu.sd_market_resourse.dto;

import dev.hgjtu.sd_market_resourse.models.Comment;

import java.time.LocalDate;
import java.util.List;

public class ItemResponse {
    private String title;
    private String description;
    private List<String> imagesUrls;
    private Integer price;
    private String location;
    private LocalDate publicationDate;
    private List<Comment> comments;
}
