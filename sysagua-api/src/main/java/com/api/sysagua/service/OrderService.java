package com.api.sysagua.service;

import com.api.sysagua.dto.order.CreateOrderDto;
import com.api.sysagua.dto.order.ViewOrderDto;
import com.api.sysagua.dto.transaction.CreateTransactionDto;
import com.api.sysagua.enumeration.DeliveryStatus;
import com.api.sysagua.enumeration.PaymentMethod;
import com.api.sysagua.enumeration.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    void create(CreateOrderDto order);

    List<ViewOrderDto> list(
            Long id,
            Long customerId,
            Long deliveryPersonId,
            Long productOrderId,
            DeliveryStatus status,
            BigDecimal receivedAmountStart,
            BigDecimal receivedAmountEnd,
            BigDecimal totalAmountStart,
            BigDecimal totalAmountEnd,
            BigDecimal balanceStart,
            BigDecimal balanceEnd,
            PaymentMethod paymentMethod,
            LocalDateTime createdAtStart,
            LocalDateTime createdAtEnd,
            LocalDateTime finishedAtStart,
            LocalDateTime finishedAtEnd,
            PaymentStatus paymentStatus
    );

    void addPayment(Long id, CreateTransactionDto dto);

    void delete(Long id);

    void finishDelivery(Long id);
}
