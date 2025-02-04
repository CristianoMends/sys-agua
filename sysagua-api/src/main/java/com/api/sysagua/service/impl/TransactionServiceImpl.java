package com.api.sysagua.service.impl;

import com.api.sysagua.dto.transaction.ViewTransactionDto;
import com.api.sysagua.enumeration.OrderStatus;
import com.api.sysagua.enumeration.PaymentMethod;
import com.api.sysagua.enumeration.TransactionStatus;
import com.api.sysagua.enumeration.TransactionType;
import com.api.sysagua.exception.BusinessException;
import com.api.sysagua.model.*;
import com.api.sysagua.repository.StockRepository;
import com.api.sysagua.repository.TransactionRepository;
import com.api.sysagua.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private StockRepository stockRepository;

    @Override
    public void save(Transaction transaction) {
        this.transactionRepository.save(transaction);
    }

    @Override
    public void cancel(Long id) {
        var transaction = this.transactionRepository.findById(id).orElseThrow(() -> new BusinessException("Transaction not found", HttpStatus.NOT_FOUND));

        transaction.setCanceledAt(LocalDateTime.now());
        transaction.setStatus(TransactionStatus.CANCELED);
        this.transactionRepository.save(transaction);
    }

    @Override
    public void finish(Long id) {
        var transaction = this.transactionRepository.findById(id).orElseThrow(() -> new BusinessException("Transaction not found", HttpStatus.NOT_FOUND));
        if (!transaction.getStatus().equals(TransactionStatus.PENDING)) throw new BusinessException("Transaction is not PENDING");

        if (transaction.getOrder() != null) {
            transaction.getOrder().setStatus(OrderStatus.FINISHED);
            processOrderProducts(transaction.getOrder());
        }
        if (transaction.getPurchase() != null) processPurchaseProducts(transaction.getPurchase());

        transaction.setFinishedAt(LocalDateTime.now());
        transaction.setStatus(TransactionStatus.PAID);
        this.transactionRepository.save(transaction);
    }

    private void addWithdrawsProductFromStock(int quantity, Product product) {
        var stock = this.stockRepository.findProduct(product.getId()).orElseThrow(
                () -> new BusinessException("Stock by product not found", HttpStatus.NOT_FOUND)
        );
        stock.setTotalWithdrawals(stock.getTotalWithdrawals() + quantity);
        this.stockRepository.save(stock);
    }
    private void addEntriesProductFromStock(int quantity, Product product) {
        var stock = this.stockRepository.findProduct(product.getId()).orElseThrow(
                () -> new BusinessException("Stock by product not found", HttpStatus.NOT_FOUND)
        );

        stock.setTotalEntries(stock.getTotalEntries() + quantity);
        this.stockRepository.save(stock);
    }
    private void processPurchaseProducts(Purchase purchase) {
        purchase.getProductPurchases()
                .forEach(p -> addEntriesProductFromStock(p.getQuantity(), p.getProduct()));
    }
    private void processOrderProducts(Order order) {
        order.getProductOrders()
                .forEach(p -> addWithdrawsProductFromStock(p.getQuantity(), p.getProduct()));
    }


    @Override
    public List<ViewTransactionDto> list(Long id, TransactionStatus status, BigDecimal amountStart, BigDecimal amountEnd, TransactionType type, PaymentMethod paymentMethod, String description, LocalDateTime createdAtStart, LocalDateTime createdAtEnd, LocalDateTime finishedAtStart, LocalDateTime finishedAtEnd, LocalDateTime canceledAtStart, LocalDateTime canceledAtEnd) {
        return this.transactionRepository.list(
                id,
                status,
                amountStart,
                amountEnd,
                type,
                paymentMethod,
                description,
                createdAtStart,
                createdAtEnd,
                finishedAtStart,
                finishedAtEnd,
                canceledAtStart,
                canceledAtEnd
        ).stream().map(Transaction::toView).toList();
    }


}
