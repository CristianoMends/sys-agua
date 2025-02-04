package com.api.sysagua.service.impl;

import com.api.sysagua.enumeration.OrderStatus;
import com.api.sysagua.enumeration.PaymentMethod;
import com.api.sysagua.exception.BusinessException;
import com.api.sysagua.model.Order;
import com.api.sysagua.repository.OrderRepository;
import com.api.sysagua.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void create(Order order) {
        this.orderRepository.save(order);


    }

    @Override
    public List<Order> list(Long id, Long customerId, Long deliveryPersonId, Long productOrderId, OrderStatus status, BigDecimal receivedAmountStart, BigDecimal receivedAmountEnd, BigDecimal totalAmountStart, BigDecimal totalAmountEnd, PaymentMethod paymentMethod, LocalDateTime createdAtStart, LocalDateTime createdAtEnd, LocalDateTime finishedAtStart, LocalDateTime finishedAtEnd) {
        return this.orderRepository.list(
                id,
                customerId,
                deliveryPersonId,
                productOrderId,
                status,
                receivedAmountStart,
                receivedAmountEnd,
                totalAmountStart,
                totalAmountEnd,
                paymentMethod,
                createdAtStart,
                createdAtEnd,
                finishedAtStart,
                finishedAtEnd
        );
    }

    @Override
    public void update(Long id, Order dto) {
        this.orderRepository.findById(id).orElseThrow(
                () -> new BusinessException("Order not found", HttpStatus.NOT_FOUND)
        );

        this.orderRepository.save(dto);
    }

    @Override
    public void delete(Long id) {

    }
}
