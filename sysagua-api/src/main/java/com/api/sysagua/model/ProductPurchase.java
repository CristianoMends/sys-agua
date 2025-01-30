package com.api.sysagua.model;

import com.api.sysagua.dto.purchase.ViewProductPurchaseDto;
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
    private BigDecimal purchasePrice;
    private BigDecimal total;

    public ProductPurchase(Purchase purchase,Product product, Integer quantity, BigDecimal purchasePrice){
        setPurchase(purchase);
        setProduct(product);
        setQuantity(quantity);
        setPurchasePrice(purchasePrice);
        this.total = purchasePrice.multiply(BigDecimal.valueOf(quantity));
    }

    public ViewProductPurchaseDto toView(){
        return new ViewProductPurchaseDto(
                getId(),
                getProduct().toView(),
                getQuantity(),
                getPurchasePrice(),
                getTotal()
        );
    }

}
