package com.api.sysagua.service.impl;

import com.api.sysagua.dto.order.*;
import com.api.sysagua.dto.transaction.CreateTransactionDto;
import com.api.sysagua.enumeration.*;
import com.api.sysagua.exception.BusinessException;
import com.api.sysagua.factory.OrderFactory;
import com.api.sysagua.model.*;
import com.api.sysagua.observer.TransactionObserver;
import com.api.sysagua.observer.TransactionSubject;
import com.api.sysagua.repository.*;
import com.api.sysagua.service.OrderService;
import com.api.sysagua.service.StockService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService, TransactionSubject {

    private final List<TransactionObserver> observers = new ArrayList<>();

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderFactory orderFactory;
    @Autowired
    private TransactionObserver transactionObserver;
    @Autowired
    private StockService stockService;

    @PostConstruct
    void init() {
        addObserver(transactionObserver);
    }

    @Override
    @Transactional
    public void create(CreateOrderDto dto) {
        var order = orderFactory.createOrder(dto);
        checkPaimentValue(order);

        if (isPaid(order)) {
            order.setPaymentStatus(PaymentStatus.PAID);
            order.setFinishedAt(LocalDateTime.now());
        } else {
            order.setPaymentStatus(PaymentStatus.PENDING);
        }
        var saved = this.orderRepository.save(order);

        if (dto.getPaidAmount() != null && dto.getPaidAmount().compareTo(BigDecimal.ZERO) > 0) {
            notifyObservers(saved, dto.getPaidAmount(), dto.getPaymentMethod(), "Pedido registrado", TransactionType.INCOME);
        }
    }

    private boolean isPaid(Order order) {
        if (order.getTotal() == null) order.calculateTotalAmount();

        return order.getPaidAmount().compareTo(order.getTotal()) >= 0;
    }

    private void checkPaimentValue(Order order) {
        order.calculateTotalAmount();
        if (order.getPaidAmount().compareTo(order.getTotal()) > 0) {
            throw new BusinessException("Amount received is greater than the total order amount");
        }
    }

    @Override
    public List<ViewOrderDto> list(Long id, Long customerId, Long deliveryPersonId, Long productOrderId, DeliveryStatus status, BigDecimal receivedAmountStart, BigDecimal receivedAmountEnd, BigDecimal totalAmountStart, BigDecimal totalAmountEnd, BigDecimal balanceStart, BigDecimal balanceEnd, PaymentMethod paymentMethod, LocalDateTime createdAtStart, LocalDateTime createdAtEnd, LocalDateTime finishedAtStart, LocalDateTime finishedAtEnd, PaymentStatus paymentStatus) {
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
                balanceStart,
                balanceEnd,
                paymentMethod,
                createdAtStart,
                createdAtEnd,
                finishedAtStart,
                finishedAtEnd,
                paymentStatus
        ).stream().map(Order::toView).toList();
    }


    @Override
    @Transactional
    public void addPayment(Long id, CreateTransactionDto dto) {
        var order = this.orderRepository.findById(id).orElseThrow(() -> new BusinessException("Order not found", HttpStatus.NOT_FOUND));
        if (isPaid(order)) throw new BusinessException("Order is paid");
        if (order.getPaymentStatus() == PaymentStatus.CANCELED) throw new BusinessException("Order is canceled");

        order.setPaidAmount(order.getPaidAmount().add(dto.getAmount()));
        checkPaimentValue(order);

        if (isPaid(order)) {
            order.setPaymentStatus(PaymentStatus.PAID);
        } else {
            order.setPaymentStatus(PaymentStatus.PENDING);
        }

        var saved = this.orderRepository.save(order);
        notifyObservers(saved, dto.getAmount(), dto.getPaymentMethod(), dto.getDescription(), TransactionType.INCOME);
    }

    @Override
    public void delete(Long id) {
        var order = this.orderRepository.findById(id).orElseThrow(() -> new BusinessException("Purchase not found", HttpStatus.NOT_FOUND));
        order.setPaymentStatus(PaymentStatus.CANCELED);
        order.setDeliveryStatus(DeliveryStatus.CANCELED);
        order.setCanceledAt(LocalDateTime.now());

        var saved = this.orderRepository.save(order);

        notifyObservers(order, order.getPaidAmount(), PaymentMethod.UNDEFINED, "Estorno de pagamentos", TransactionType.EXPENSE);
        processProductRefunds(saved);
    }

    @Override
    public void finishDelivery(Long id) {
        var order = this.orderRepository.findById(id).orElseThrow(() -> new BusinessException("Purchase not found", HttpStatus.NOT_FOUND));

        switch (order.getDeliveryStatus()) {
            case CANCELED -> throw new BusinessException("Order is canceled");
            case FINISHED -> throw new BusinessException("Order is finished");
        }

        order.setDeliveryStatus(DeliveryStatus.FINISHED);
        order.setFinishedAt(LocalDateTime.now());
        var saved = this.orderRepository.save(order);

        processOrderProducts(saved);
    }

    private void addWithdrawsProductFromStock(int quantity, Product product) {
        this.stockService.addWithdraw(quantity,product);
    }

    private void processOrderProducts(Order order) {
        order.getProductOrders()
                .forEach(p -> addWithdrawsProductFromStock(p.getQuantity(), p.getProduct()));

    }

    private void remWithdrawsProductOnStock(int quantity, Product product) {
        this.stockService.processStockReturn(product.getId(), quantity, "Cancelamento de pedido");
    }

    private void processProductRefunds(Order order) {
        order.getProductOrders()
                .forEach(p -> remWithdrawsProductOnStock(p.getQuantity(), p.getProduct()));
    }

    @Override
    public void addObserver(TransactionObserver observer) {
        if (this.observers.contains(observer)) return;

        this.observers.add(observer);
    }

    @Override
    public void removeObserver(TransactionObserver observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers(Transactable transactable, BigDecimal amount, PaymentMethod paymentMethod, String description, TransactionType transactionType) {
        for (var o : this.observers) {
            o.update(transactable, amount, paymentMethod, description, transactionType);
        }
    }
}
