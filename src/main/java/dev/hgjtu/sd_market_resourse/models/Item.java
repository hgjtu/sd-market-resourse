package dev.hgjtu.sd_market_resourse.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Table("items")
public class Item {
    @Id
    private Long id;

    @Column("category_id")
    private Integer categoryId;

    @Column("title")
    private String title;

    @Column("description")
    private String description;

    @Column("images_urls")
    private List<String> imagesUrls;

    @Column("price")
    private Integer price;

    @Column("location")
    private String location;

    public Item() {
    }

    public Item(Long id, Integer categoryId, String title, String description, List<String> imagesUrls, Integer price, String location) {
        this.id = id;
        this.categoryId = categoryId;
        this.title = title;
        this.description = description;
        this.imagesUrls = imagesUrls;
        this.price = price;
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImagesUrls() {
        return imagesUrls;
    }

    public void setImagesUrls(List<String> imagesUrls) {
        this.imagesUrls = imagesUrls;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
