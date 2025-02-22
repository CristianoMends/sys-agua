package com.api.sysagua.service.impl;

import com.api.sysagua.dto.purchase.*;
import com.api.sysagua.dto.stock.AddProductDto;
import com.api.sysagua.dto.transaction.CreateTransactionDto;
import com.api.sysagua.enumeration.*;
import com.api.sysagua.exception.BusinessException;
import com.api.sysagua.factory.PurchaseFactory;
import com.api.sysagua.model.*;
import com.api.sysagua.observer.TransactionSubject;
import com.api.sysagua.observer.TransactionObserver;
import com.api.sysagua.repository.*;
import com.api.sysagua.service.PurchaseService;
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
public class PurchaseServiceImpl implements PurchaseService, TransactionSubject {

    private final List<TransactionObserver> observers = new ArrayList<>();

    @Autowired
    private PurchaseFactory purchaseFactory;
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private StockService stockService;
    @Autowired
    private TransactionObserver transactionObserver;

    @PostConstruct
    void init() {
        addObserver(transactionObserver);
    }

    @Override
    @Transactional
    public void create(CreatePurchaseDto dto) {
        var purchase = purchaseFactory.createPurchase(dto);
        checkPaimentValue(purchase);

        if (isPaid(purchase)) {
            purchase.setPaymentStatus(PaymentStatus.PAID);
            purchase.setFinishedAt(LocalDateTime.now());
        } else {
            purchase.setPaymentStatus(PaymentStatus.PENDING);
        }

        var saved = this.purchaseRepository.save(purchase);

        if (dto.getPaidAmount() != null && dto.getPaidAmount().compareTo(BigDecimal.ZERO) > 0) {
            notifyObservers(saved, dto.getPaidAmount().negate(), dto.getPaymentMethod(), "Compra registrada");
        }

        processProductsOnStock(saved);
    }

    @Override
    @Transactional
    public void addPayment(Long id, CreateTransactionDto dto) {
        var purchase = this.purchaseRepository.findById(id).orElseThrow(() -> new BusinessException("Purchase not found", HttpStatus.NOT_FOUND));
        if (!purchase.getActive() || isPaid(purchase)) return;

        purchase.setPaidAmount(purchase.getPaidAmount().add(dto.getAmount()));

        checkPaimentValue(purchase);

        if (isPaid(purchase)) {
            purchase.setPaymentStatus(PaymentStatus.PAID);
        } else {
            purchase.setPaymentStatus(PaymentStatus.PENDING);
        }

        var saved = this.purchaseRepository.save(purchase);
        notifyObservers(saved, dto.getAmount().negate(), dto.getPaymentMethod(), dto.getDescription());
    }

    @Override
    public void delete(Long id) {
        var purchase = this.purchaseRepository.findById(id).orElseThrow(() -> new BusinessException("Purchase not found", HttpStatus.NOT_FOUND));
        purchase.setActive(false);
        purchase.setPaymentStatus(PaymentStatus.CANCELED);
        purchase.setCanceledAt(LocalDateTime.now());

        var saved = this.purchaseRepository.save(purchase);

        notifyObservers(purchase, purchase.getPaidAmount(), PaymentMethod.UNDEFINED, "Estorno de pagamentos");
        processProductRefunds(saved);
    }

    @Override
    public List<ViewPurchaseDto> list(
            Long id,
            BigDecimal totalAmountStart,
            BigDecimal totalAmountEnd,
            BigDecimal paidAmountStart,
            BigDecimal paidAmountEnd,
            Boolean active,
            LocalDateTime entryAtStart,
            LocalDateTime entryAtEnd,
            LocalDateTime createdAtStart,
            LocalDateTime createdAtEnd,
            LocalDateTime finishedAtStart,
            LocalDateTime finishedAtEnd,
            LocalDateTime canceledAtStart,
            LocalDateTime canceledAtEnd,
            String description,
            Long supplierId,
            Long productId,
            String nfe,
            PaymentMethod paymentMethod,
            PaymentStatus paymentStatus
    ) {
        return purchaseRepository.list(
                id,
                totalAmountStart,
                totalAmountEnd,
                paidAmountStart,
                paidAmountEnd,
                active,
                entryAtStart,
                entryAtEnd,
                createdAtStart,
                createdAtEnd,
                finishedAtStart,
                finishedAtEnd,
                canceledAtStart,
                canceledAtEnd,
                description,
                supplierId,
                productId,
                nfe,
                paymentMethod,
                paymentStatus
        ).stream().map(Purchase::toView).toList();
    }

    private void checkPaimentValue(Purchase purchase) {
        purchase.calculateTotalAmount();
        if (purchase.getPaidAmount().compareTo(purchase.getTotal()) > 0) {
            throw new BusinessException("Amount paid is greater than the total purchase amount");
        }
    }

    private boolean isPaid(Purchase purchase) {
        if (purchase.getTotal() == null) purchase.calculateTotalAmount();

        return purchase.getPaidAmount().compareTo(purchase.getTotal()) >= 0;
    }

    private void addEntriesProductOnStock(int quantity, Product product) {
        this.stockService.addProduct(new AddProductDto(product.getId(), quantity));
    }

    private void remEntriesProductOnStock(int quantity, Product product) {
        this.stockService.processStockReturn(product.getId(), (quantity*-1),"Cancelamento de compra");
    }

    private void processProductsOnStock(Purchase purchase) {
        purchase.getProductPurchases()
                .forEach(p -> addEntriesProductOnStock(p.getQuantity(), p.getProduct()));

    }

    private void processProductRefunds(Purchase purchase) {
        purchase.getProductPurchases()
                .forEach(p -> remEntriesProductOnStock(p.getQuantity(), p.getProduct()));
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
    public void notifyObservers(Transactable purchase, BigDecimal amount, PaymentMethod paymentMethod, String description) {
        for (var o : observers) {
            o.update((Purchase) purchase, amount, paymentMethod, description);
        }
    }
}
