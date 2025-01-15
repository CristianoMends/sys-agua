package com.api.sysagua.model;

import com.api.sysagua.dto.product.ViewProductDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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

    @Column(nullable = false)
    private String unit;

    private String brand;

    private String category;

    private LocalDate registeredAt;

    private LocalDate updatedAt;

    public Product(String name, String unit, String brand, String category) {
        this.name = name;
        this.unit = unit;
        this.brand = brand;
        this.category = category;
    }

    public ViewProductDto toView() {
        return new ViewProductDto(
                this.id,
                this.name,
                this.unit,
                this.brand,
                this.category,
                this.registeredAt,
                this.updatedAt
        );
    }
}
