package com.api.sysagua.model;

import com.api.sysagua.dto.purchase.ViewPurchaseDto;
import com.api.sysagua.enumeration.PaymentMethod;
import com.api.sysagua.enumeration.PaymentStatus;
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "id")
    private Long id;
    private BigDecimal paidAmount;
    private BigDecimal totalAmount;
    private BigDecimal balance;
    private Boolean active;
    private LocalDateTime entryAt;
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;
    private LocalDateTime canceledAt;
    private String description;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    private String nfe;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ProductPurchase> productPurchases;
    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @PrePersist
    private void prePersist(){
        calculateTotalAmount();
        setCreatedAt(LocalDateTime.now());
        setBalance(totalAmount.subtract(paidAmount));
    }

    @PreUpdate
    private void preUpdate(){
        setActive(true);
    }

    @Override
    public void calculateTotalAmount() {
        if (getTotalAmount() != null) return;

        this.totalAmount = productPurchases.stream()
                .map(p -> p.getPurchasePrice().multiply(BigDecimal.valueOf(p.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public ViewPurchaseDto toView(){
        return new ViewPurchaseDto(
                getId(),
                getPaidAmount(),
                getTotalAmount(),
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
                getProductPurchases().stream().map(ProductPurchase::toView).toList(),
                getSupplier()
        );
    }
}
