package com.api.sysagua.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "initial_quantity", nullable = false)
    private int initialQuantity;

    @Column(name = "total_entries", nullable = false)
    private int totalEntries;

    @Column(name = "total_withdrawals", nullable = false)
    private int totalWithdrawals;

    @Transient
    private int currentQuantity;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @PostLoad
    public void calculateCurrentQuantity() {
        this.currentQuantity = initialQuantity + totalEntries - totalWithdrawals;
    }

}
