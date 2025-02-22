package com.api.sysagua.model;

import com.api.sysagua.dto.productItem.ViewProductItemDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product_purchases")
public class ProductPurchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal total;

    @PostLoad
    private void calculateItemTotal() {
        this.total = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public ProductPurchase(Purchase purchase,Product product, Integer quantity, BigDecimal purchasePrice){
        setPurchase(purchase);
        setProduct(product);
        setQuantity(quantity);
        setUnitPrice(purchasePrice);
    }

    public ViewProductItemDto toView(){
        return new ViewProductItemDto(
                getId(),
                getQuantity(),
                getUnitPrice(),
                getTotal(),
                getProduct().toView()
        );
    }

}
