package dev.hgjtu.sd_market_resourse.dto;

import java.util.List;

public class ItemRequest {
    private String title;
    private Integer categoryId;
    private String description;
    private Integer price;
    private String location;
    private String type;

    public String getTitle() {
        return title;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public String getDescription() {
        return description;
    }

    public Integer getPrice() {
        return price;
    }

    public String getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }
}
