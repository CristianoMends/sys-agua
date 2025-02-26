package com.api.sysagua.model;

import com.api.sysagua.dto.purchase.ViewPurchaseDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "purchases")
@Getter
@Setter
@NoArgsConstructor
public class Purchase extends Transactable {

    private Boolean active;
    private LocalDateTime entryAt;
    private String nfe;
    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ProductPurchase> productPurchases;
    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @PrePersist
    private void prePersist(){
        calculateTotalAmount();
        setCreatedAt(LocalDateTime.now());
        setBalance(getTotal().subtract(getPaidAmount()));
    }

    @PostLoad
    private void onLoad(){
        setBalance(getTotal().subtract(getPaidAmount()));
    }

    @PreUpdate
    private void preUpdate(){
        setActive(true);
        calculateTotalAmount();
        setBalance(getTotal().subtract(getPaidAmount()));
    }

    @Override
    public void calculateTotalAmount() {
        if (getTotal() != null) return;

        var total = productPurchases.stream()
                .map(p -> p.getUnitPrice().multiply(BigDecimal.valueOf(p.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        setTotal(total);
    }

    public ViewPurchaseDto toView(){
        return new ViewPurchaseDto(
                getId(),
                getPaidAmount(),
                getTotal(),
                getBalance(),
                getCreatedAt(),
                getEntryAt(),
                getCanceledAt(),
                getFinishedAt(),
                getPaymentMethod(),
                getPaymentStatus(),
                getNfe(),
                getDescription(),
                getActive(),
                getSupplier(),
                getProductPurchases().stream().map(ProductPurchase::toView).toList()
        );
    }
}
