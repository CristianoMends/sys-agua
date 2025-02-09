package com.api.sysagua.service.impl;

import com.api.sysagua.dto.cashier.ViewCashierDto;
import com.api.sysagua.enumeration.PaymentStatus;
import com.api.sysagua.enumeration.TransactionStatus;
import com.api.sysagua.enumeration.TransactionType;
import com.api.sysagua.model.Cashier;
import com.api.sysagua.model.Transaction;
import com.api.sysagua.repository.OrderRepository;
import com.api.sysagua.repository.PurchaseRepository;
import com.api.sysagua.repository.TransactionRepository;
import com.api.sysagua.service.CashierService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Service
public class CashierServiceImpl implements CashierService {


    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PurchaseRepository purchaseRepository;

    private Cashier cashier;

    @PostConstruct
    private void initCashier() {
        this.cashier = new Cashier(BigDecimal.ZERO);
    }

    @Override
    public void addBalance(BigDecimal balance) {
        createAddBalanceTransaction(balance);
        calculateBalance();
    }

    @Override
    public ViewCashierDto list(
            Long transactionId,
            TransactionStatus transactionStatus,
            BigDecimal amountStart,
            BigDecimal amountEnd,
            TransactionType type,
            String description,
            LocalDateTime createdAtStart,
            LocalDateTime createdAtEnd,
            Long orderId,
            Long purchaseId
    ) {
        var transactions = this.transactionRepository.list(
                transactionId,
                transactionStatus,
                amountStart,
                amountEnd,
                type,
                description,
                createdAtStart,
                createdAtEnd,
                orderId,
                purchaseId
        );
        this.cashier.setTransactions(transactions);
        calculateBalance();
        return cashier.toView();
    }

    private void createAddBalanceTransaction(BigDecimal balance) {
        var t = new Transaction(
                TransactionStatus.PAID,
                balance,
                TransactionType.INCOME,
                "Incremento de saldo por usuário",
                null,
                null
        );
        this.transactionRepository.save(t);
    }

    private void calculateBalance() {
        BigDecimal balance = BigDecimal.ZERO;

        for (var order : orderRepository.findAll()) {
            if (order.getPaymentStatus() == PaymentStatus.CANCELED) continue;

            var income = this.transactionRepository.countReceivedAmountForOrder(TransactionType.INCOME, order.getId());
            var expense = this.transactionRepository.countReceivedAmountForOrder(TransactionType.EXPENSE, order.getId());

            if (income.isPresent()) balance = balance.add(income.get());
            if (expense.isPresent()) balance = balance.subtract(expense.get());
        }

        for (var purchase : purchaseRepository.findAll()) {
            if (purchase.getPaymentStatus() == PaymentStatus.CANCELED) continue;

            var income = this.transactionRepository.countReceivedAmountForPurchase(TransactionType.INCOME, purchase.getId());
            var expense = this.transactionRepository.countReceivedAmountForPurchase(TransactionType.EXPENSE, purchase.getId());


            if (income.isPresent()) balance = balance.add(income.get());
            if (expense.isPresent()) balance = balance.subtract(expense.get());
        }

        this.cashier.setBalance(balance);
    }

}
