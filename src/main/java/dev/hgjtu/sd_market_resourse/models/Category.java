package dev.hgjtu.sd_market_resourse.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("categories")
public class Category {
    @Id
    private Integer id;

    @Column("name")
    private String name;

    @Column("name_ru")
    private String nameRu;

    @Column("short_description")
    private String shortDescription;

    @Column("description")
    private String description;

    @Column("category_media")
    private UUID categoryMedia;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameRu() {
        return nameRu;
    }

    public void setNameRu(String nameRu) {
        this.nameRu = nameRu;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getCategoryMedia() {
        return categoryMedia;
    }

    public void setCategoryMedia(UUID categoryMedia) {
        this.categoryMedia = categoryMedia;
    }
}
