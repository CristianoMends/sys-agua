package com.api.sysagua.model;

import com.api.sysagua.dto.order.ViewOrderDto;
import com.api.sysagua.enumeration.OrderStatus;
import com.api.sysagua.enumeration.PaymentMethod;
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
    private OrderStatus status;

    private BigDecimal receivedAmount;
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime finishedAt;

    public Order(Customer customer, DeliveryPerson deliveryPerson, List<ProductOrder> productOrders, OrderStatus status) {
        setCustomer(customer);
        setDeliveryPerson(deliveryPerson);
        setProductOrders(productOrders);
        setStatus(status);
    }
    @PrePersist
    private void calculateTotalAmount(){
        if (getTotalAmount() != null) return;

        this.totalAmount = productOrders.stream()
                .map(p -> p.getUnitPrice()
                        .multiply(BigDecimal.valueOf(p.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public ViewOrderDto toView() {
        return new ViewOrderDto(
                getId(),
                getStatus(),
                getReceivedAmount(),
                getTotalAmount(),
                getPaymentMethod(),
                getCreatedAt(),
                getFinishedAt(),
                getCustomer(),
                getDeliveryPerson(),
                getProductOrders().stream().map(ProductOrder::toView).toList()
        );
    }
}