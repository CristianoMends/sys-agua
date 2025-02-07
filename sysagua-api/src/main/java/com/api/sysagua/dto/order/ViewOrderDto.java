package com.api.sysagua.dto.order;

import com.api.sysagua.enumeration.DeliveryStatus;
import com.api.sysagua.enumeration.PaymentMethod;
import com.api.sysagua.model.Customer;
import com.api.sysagua.model.DeliveryPerson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ViewOrderDto {

    private Long id;
    private DeliveryStatus status;
    private BigDecimal receivedAmount;
    private BigDecimal totalAmount;
    private PaymentMethod paymentMethod;
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;
    private LocalDateTime canceledAt;
    private String description;
    private Customer customer;
    private DeliveryPerson deliveryPerson;
    private List<ViewProductOrderDto> productOrders;

}
