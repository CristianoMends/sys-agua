package com.api.sysagua.model;

import com.api.sysagua.dto.transaction.ViewTransactableDto;
import com.api.sysagua.dto.transaction.ViewTransactionDto;
import com.api.sysagua.dto.user.ViewUserDto;
import com.api.sysagua.enumeration.PaymentMethod;
import com.api.sysagua.enumeration.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "responsible_id")
    private User responsibleUser;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;


    public Transaction(BigDecimal amount, TransactionType type, PaymentMethod paymentMethod, String description, User responsibleUser, Transactable transactable) {
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.responsibleUser = responsibleUser;
        this.paymentMethod = paymentMethod;

        if (transactable instanceof Order) {
            this.order = (Order) transactable;
        } else if (transactable instanceof Purchase) {
            this.purchase = (Purchase) transactable;
        }
    }

    @PrePersist
    private void prePersist() {
        setCreatedAt(LocalDateTime.now());
    }

    public ViewTransactionDto toView() {
        return new ViewTransactionDto(
                getId(),
                getCreatedAt(),
                getAmount(),
                getType(),
                getPaymentMethod(),
                getDescription(),
                getViewResponsibleUser(),
                getViewTransactable()
        );
    }

    public ViewUserDto getViewResponsibleUser() {
        return getResponsibleUser() != null ? getResponsibleUser().toView() : null;
    }

    ViewTransactableDto getViewTransactable() {
        return purchase != null ? purchase.toView() : order != null ? order.toView() : null;
    }
}
