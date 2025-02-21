package com.api.sysagua.service.impl;

import com.api.sysagua.enumeration.PaymentMethod;
import com.api.sysagua.enumeration.TransactionType;
import com.api.sysagua.model.Order;
import com.api.sysagua.model.Purchase;
import com.api.sysagua.model.Transactable;
import com.api.sysagua.model.Transaction;
import com.api.sysagua.observer.TransactionObserver;
import com.api.sysagua.repository.TransactionRepository;
import com.api.sysagua.service.TransactionService;
import com.api.sysagua.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionServiceImpl implements TransactionService, TransactionObserver {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserService userService;

    @Override
    public void update(Transactable transactable, BigDecimal amount, PaymentMethod paymentMethod, String description) {
        if (transactable instanceof Order) {
            createTransaction((Order) transactable, amount, paymentMethod, description);
        } else {
            createTransaction((Purchase) transactable, amount, paymentMethod, description);
        }
    }

    private void createTransaction(Order order, BigDecimal amount, PaymentMethod paymentMethod, String description) {
        var user = this.userService.getLoggedUser();
        var t = new Transaction(
                amount,
                TransactionType.EXPENSE,
                paymentMethod,
                description,
                user,
                order
        );
        this.transactionRepository.save(t);
    }

    private void createTransaction(Purchase purchase, BigDecimal amount, PaymentMethod paymentMethod, String description) {
        var user = this.userService.getLoggedUser();
        var t = new Transaction(
                amount,
                TransactionType.EXPENSE,
                paymentMethod,
                description,
                user,
                purchase
        );
        this.transactionRepository.save(t);
    }
}
