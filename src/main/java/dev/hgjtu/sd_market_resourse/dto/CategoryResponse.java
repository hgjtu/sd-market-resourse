package dev.hgjtu.sd_market_resourse.dto;

import dev.hgjtu.sd_market_resourse.models.Category;

public class CategoryResponse {
    private Integer id;
    private String name;
    private String nameRu;
    private String shortDescription;
    private String description;
    private String categoryMediaURL;

    public CategoryResponse(Integer id, String name, String nameRu, String shortDescription, String description, String categoryMediaURL) {
        this.id = id;
        this.name = name;
        this.nameRu = nameRu;
        this.shortDescription = shortDescription;
        this.description = description;
        this.categoryMediaURL = categoryMediaURL;
    }

    public CategoryResponse(Category category, String categoryMediaURL) {
        this.id = category.getId();
        this.name = category.getName();
        this.nameRu = category.getNameRu();
        this.shortDescription = category.getShortDescription();
        this.description = category.getDescription();
        this.categoryMediaURL = categoryMediaURL;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNameRu() {
        return nameRu;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getCategoryMediaURL() {
        return categoryMediaURL;
    }
}