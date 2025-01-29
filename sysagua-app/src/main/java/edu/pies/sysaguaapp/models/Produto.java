package edu.pies.sysaguaapp.models;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Produto {
    private Long id;
    private String name;
    private String unit;
    private BigDecimal price;
    private BigDecimal cost;
    private String brand;
    private Long categoryId;
    private Long lineId;
    private String ncm;
    private String createdAt;
    private String updatedAt;
    private Boolean active;

    private ProductCategory category;
    private ProductLine line;

    public void setCategory(ProductCategory category) {
        this.category = category;
        this.categoryId = category != null ? category.getId() : null;
    }

    public void setLine(ProductLine line) {
        this.line = line;
        this.lineId = line != null ? line.getId() : null;
    }

    public boolean isActive() {
        return active;
    }
}

