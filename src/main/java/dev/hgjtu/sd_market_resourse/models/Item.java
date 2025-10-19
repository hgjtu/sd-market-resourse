package dev.hgjtu.sd_market_resourse.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.List;

@Table("items")
public class Item {
    @Id
    private Long id;

    @Column("category_id")
    private Integer categoryId;

    @Column("user_id")
    private Long userId;

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

    @Column("type")
    private String type;

    @Column("publication_date")
    private LocalDate publicationDate;

    public Item() {
    }

    public Item(Long id, Integer categoryId, Long userId, String title,
                String description, List<String> imagesUrls,
                Integer price, String location, String type,
                LocalDate publicationDate) {
        this.id = id;
        this.categoryId = categoryId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.imagesUrls = imagesUrls;
        this.price = price;
        this.location = location;
        this.type = type;
        this.publicationDate = publicationDate;
    }

    public Item(Integer categoryId, Long userId,
                String title, String description, List<String> imagesUrls,
                Integer price, String location, String type, LocalDate publicationDate) {
        this.categoryId = categoryId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.imagesUrls = imagesUrls;
        this.price = price;
        this.location = location;
        this.type = type;
        this.publicationDate = publicationDate;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
