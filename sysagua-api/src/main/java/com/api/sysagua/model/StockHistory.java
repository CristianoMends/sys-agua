package com.api.sysagua.model;

import com.api.sysagua.dto.stockHistory.ViewStockHistoryDto;
import com.api.sysagua.enumeration.MovementType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class StockHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MovementType type;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(nullable = false, updatable = false)
    private LocalDateTime date;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "responsible_id")
    private User responsibleUser;

    @ManyToOne
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    public StockHistory(User responsibleUser, Stock stock, MovementType movementType, int quantity, String description) {
        this.responsibleUser = responsibleUser;
        this.stock = stock;
        this.type = movementType;
        this.quantity = quantity;
        this.description = description;
    }

    @PrePersist
    protected void onCreate() {
        this.date = LocalDateTime.now();
    }

    public ViewStockHistoryDto toView(){
        return new ViewStockHistoryDto(
                getId(),
                getType(),
                getQuantity(),
                getDate(),
                getDescription(),
                getResponsibleUser()!= null?getResponsibleUser().toView():null,
                getStock()
        );
    }
}
