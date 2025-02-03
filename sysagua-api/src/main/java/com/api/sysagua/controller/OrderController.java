package com.api.sysagua.controller;

import com.api.sysagua.docs.OrderDoc;
import com.api.sysagua.dto.order.CreateOrderDto;
import com.api.sysagua.dto.order.UpdateOrderDto;
import com.api.sysagua.enumeration.OrderStatus;
import com.api.sysagua.enumeration.PaymentMethod;
import com.api.sysagua.model.Customer;
import com.api.sysagua.model.DeliveryPerson;
import com.api.sysagua.model.Order;
import com.api.sysagua.model.ProductOrder;
import com.api.sysagua.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("orders")
public class OrderController implements OrderDoc{

    @Autowired
    private OrderService service;

    @CrossOrigin
    @PostMapping
    public ResponseEntity<Void> create(
            @RequestBody @Valid CreateOrderDto dto){
                this.service.createOrder(dto);
            return ResponseEntity.status(HttpStatus.CREATED).build();
    }

@GetMapping
@CrossOrigin
public ResponseEntity<List<Order>> list(
        @RequestParam(required = false) Long id,
        @RequestParam(required = false) Customer customer,
        @RequestParam(required = false) DeliveryPerson deliveryPerson,
        @RequestParam(required = false) List<ProductOrder> productOrders,
        @RequestParam(required = false) OrderStatus status,
        @RequestParam(required = false) BigDecimal receivedAmount,
        @RequestParam(required = false) BigDecimal totalAmount,
        @RequestParam(required = false) PaymentMethod paymentMethod,
        @RequestParam(required = false) LocalDateTime createdAt,
        @RequestParam(required = false) LocalDateTime finishedAt
){
    List<Order> orders = service.findByFilters(id, customer, deliveryPerson, productOrders, status,
            receivedAmount, totalAmount, paymentMethod, createdAt, finishedAt);
    return ResponseEntity.ok(orders);
}

@CrossOrigin
@DeleteMapping("{id")
public ResponseEntity<Void> delete(@PathVariable Long id){
    this.service.deleteOrder(id);
    return ResponseEntity.noContent().build();
}

    @PutMapping("{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid UpdateOrderDto dto){
        this.service.updateOrder(id, dto);
        return ResponseEntity.noContent().build();
    }
}

