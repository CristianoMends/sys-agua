package com.api.sysagua.service.impl;

import com.api.sysagua.dto.order.CreateOrderDto;
import com.api.sysagua.dto.order.CreateProductOrderDto;
import com.api.sysagua.dto.order.UpdateOrderDto;
import com.api.sysagua.dto.order.ViewOrderDto;
import com.api.sysagua.enumeration.OrderStatus;
import com.api.sysagua.enumeration.PaymentMethod;
import com.api.sysagua.enumeration.TransactionType;
import com.api.sysagua.exception.BusinessException;
import com.api.sysagua.model.*;
import com.api.sysagua.repository.*;
import com.api.sysagua.service.OrderService;
import com.api.sysagua.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private DeliveryPersonRepository deliveryPersonRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private TransactionService transactionService;

    @Override
    public void create(CreateOrderDto dto) {
        var order = new Order();
        order.setCustomer(getCustomerById(dto.getCustomerId()));
        order.setDeliveryPerson(getDeliveryPersonById(dto.getDeliveryPersonId()));
        order.setProductOrders(createListProductOrder(order, dto.getProductOrders()));
        order.setStatus(OrderStatus.PENDING);
        order.setReceivedAmount(dto.getReceivedAmount());
        order.setTotalAmount(dto.getTotalAmount());
        order.setPaymentMethod(dto.getPaymentMethod());
        var saved = this.orderRepository.save(order);

        this.saveOnTransactionHistory(saved);
    }

    private DeliveryPerson getDeliveryPersonById(Long id) {
        return this.deliveryPersonRepository.findById(id).orElseThrow(
                () -> new BusinessException("Delivery Person with id " + id + " not found", HttpStatus.NOT_FOUND)
        );
    }

    private Customer getCustomerById(Long id) {
        return this.customerRepository.findById(id).orElseThrow(
                () -> new BusinessException("Customer with id " + id + " not found", HttpStatus.NOT_FOUND)
        );
    }

    private Product getProductById(Long id) {
        return this.productRepository.findById(id).orElseThrow(
                () -> new BusinessException("Product with id " + id + " not found", HttpStatus.NOT_FOUND)
        );
    }

    private List<ProductOrder> createListProductOrder(Order order, List<CreateProductOrderDto> dtos) {
        var list = new ArrayList<ProductOrder>();

        dtos.forEach(createProductOrderDto -> {
            var product = this.getProductById(createProductOrderDto.getProductId());

            list.add(new ProductOrder(
                    order,
                    product,
                    createProductOrderDto.getQuantity(),
                    createProductOrderDto.getUnitPrice()
            ));
        });

        return list;
    }

    private void saveOnTransactionHistory(Order order) {
        this.transactionService.save(
                order.getTotalAmount(),
                TransactionType.INCOME,
                order.getPaymentMethod(),
                "Pedido ID: " + order.getId()
        );
    }

    @Override
    public List<ViewOrderDto> list(Long id, Long customerId, Long deliveryPersonId, Long productOrderId, OrderStatus status, BigDecimal receivedAmountStart, BigDecimal receivedAmountEnd, BigDecimal totalAmountStart, BigDecimal totalAmountEnd, PaymentMethod paymentMethod, LocalDateTime createdAtStart, LocalDateTime createdAtEnd, LocalDateTime finishedAtStart, LocalDateTime finishedAtEnd) {
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
        ).stream().map(Order::toView).toList();
    }

    @Override
    public void update(Long id, UpdateOrderDto dto) {
        var order = this.orderRepository.findById(id).orElseThrow(
                () -> new BusinessException("Order not found", HttpStatus.NOT_FOUND)
        );


        this.orderRepository.save(order);
    }
}
