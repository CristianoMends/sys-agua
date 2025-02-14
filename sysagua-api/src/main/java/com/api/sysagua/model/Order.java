package com.api.sysagua.model;

import com.api.sysagua.dto.order.ViewOrderDto;
import com.api.sysagua.enumeration.DeliveryStatus;
import com.api.sysagua.enumeration.PaymentMethod;
import com.api.sysagua.enumeration.PaymentStatus;
import lombok.*;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "delivery_person_id")
    private DeliveryPerson deliveryPerson;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductOrder> productOrders;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    private BigDecimal receivedAmount;
    private BigDecimal totalAmount;
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @CreatedDate
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;
    private LocalDateTime canceledAt;
    private String description;


    @PrePersist
    private void prePersist(){
        calculateTotalAmount();
        setCreatedAt(LocalDateTime.now());
    }
    public void calculateTotalAmount(){
        if (getTotalAmount() != null) return;

        this.totalAmount = productOrders.stream()
                .map(p -> p.getUnitPrice()
                        .multiply(BigDecimal.valueOf(p.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public ViewOrderDto toView() {
        return new ViewOrderDto(
                getId(),
                getDeliveryStatus(),
                getPaymentStatus(),
                getReceivedAmount(),
                getTotalAmount(),
                getPaymentMethod(),
                getCreatedAt(),
                getFinishedAt(),
                getCanceledAt(),
                getDescription(),
                getCustomer(),
                getDeliveryPerson(),
                getProductOrders().stream().map(ProductOrder::toView).toList()
        );
    }
}