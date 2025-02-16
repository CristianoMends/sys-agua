package com.api.sysagua.model;

import com.api.sysagua.dto.transaction.ViewTransactionDto;
import com.api.sysagua.enumeration.PaymentMethod;
import com.api.sysagua.enumeration.TransactionStatus;
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

    public Transaction(BigDecimal amount, TransactionType type, String description, User responsibleUser, Order order, Purchase purchase) {
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.responsibleUser = responsibleUser;
        this.order = order;
        this.purchase = purchase;
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
                getDescription(),
                getResponsibleUser()    != null ? getResponsibleUser()  : null,
                getOrder()              != null ? getOrder().toView()   : null,
                getPurchase()           != null ? getPurchase().toView() : null
        );
    }
}
