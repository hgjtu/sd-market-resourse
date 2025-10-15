package dev.hgjtu.sd_market_resourse.models;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Table("categories")
public class Category {
    @Id
    private Integer id;

    @Column("name")
    private String name;
}
