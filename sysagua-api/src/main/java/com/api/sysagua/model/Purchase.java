package com.api.sysagua.model;


import com.api.sysagua.dto.purchase.ViewPurchaseDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "purchases")
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "purchase_id")
    private Long id;
    private Double totalValue;
    private Boolean active;
    private ZonedDateTime updatedAt;
    private ZonedDateTime createdAt;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ProductPurchase> productPurchases;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;


    public void updateTotalValue() {
        this.totalValue = productPurchases.stream()
                .mapToDouble(p -> p.getPurchasePrice() * p.getQuantity())
                .sum();
    }

    public ViewPurchaseDto toView(){
        return new ViewPurchaseDto(
                getId(),
                getTotalValue(),
                getCreatedAt(),
                getUpdatedAt(),
                getActive(),
                getProductPurchases().stream().map(ProductPurchase::toView).toList(),
                getSupplier()
        );
    }


}
