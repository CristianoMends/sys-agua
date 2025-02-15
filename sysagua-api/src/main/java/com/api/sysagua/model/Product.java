package com.api.sysagua.model;

import com.api.sysagua.dto.product.ViewProductDto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Products")
@ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    private BigDecimal price;

    private BigDecimal cost;

    @Column(nullable = false)
    private String unit;

    private String brand;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ProductCategory category;


    @ManyToOne
    @JoinColumn(name = "line_id")
    private ProductLine line;

    private String ncm;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean active;

    public Product(String name, String unit, String brand, ProductCategory category, Boolean active) {
        this.name = name;
        this.unit = unit;
        this.brand = brand;
        this.category = category;
        this.active = active;
    }

    public ViewProductDto toView() {
        return new ViewProductDto(
                this.id,
                this.name,
                this.price,
                this.cost,
                this.unit,
                this.brand,
                this.category,
                this.createdAt,
                this.updatedAt,
                this.active,
                this.line,
                this.ncm
        );
    }
}
