package dev.hgjtu.sd_market_resourse.dto;

import dev.hgjtu.sd_market_resourse.models.Comment;

import java.util.List;

public class ItemResponse {
    private Long id;
    private String title;
    private String description;
    private List<String> imagesUrls;
    private Integer price;
    private List<Comment> comments;
}
