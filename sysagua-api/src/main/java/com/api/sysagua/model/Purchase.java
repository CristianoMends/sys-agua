package com.api.sysagua.model;


import com.api.sysagua.dto.purchase.ViewPurchaseDto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "purchases")
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "purchase_id")
    private Long id;
    private BigDecimal totalValue;
    private Boolean active;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;
    private LocalDateTime canceledAt;
    private String description;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ProductPurchase> productPurchases;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @PrePersist
    private void prePersist(){
        updateTotalValue();
    }

    private void updateTotalValue() {
        this.totalValue = productPurchases.stream()
                .map(p -> p.getPurchasePrice().multiply(BigDecimal.valueOf(p.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public ViewPurchaseDto toView(){
        return new ViewPurchaseDto(
                getId(),
                getTotalValue(),
                getCreatedAt(),
                getUpdatedAt(),
                getCanceledAt(),
                getFinishedAt(),
                getDescription(),
                getActive(),
                getProductPurchases().stream().map(ProductPurchase::toView).toList(),
                getSupplier()
        );
    }


}
