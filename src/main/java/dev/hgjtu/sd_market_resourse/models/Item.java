package dev.hgjtu.sd_market_resourse.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
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
}
