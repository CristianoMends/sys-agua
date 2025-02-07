package com.api.sysagua.service.impl;

import com.api.sysagua.dto.cashier.ViewCashierDto;
import com.api.sysagua.enumeration.PaymentMethod;
import com.api.sysagua.enumeration.TransactionStatus;
import com.api.sysagua.enumeration.TransactionType;
import com.api.sysagua.model.Cashier;
import com.api.sysagua.model.Transaction;
import com.api.sysagua.repository.TransactionRepository;
import com.api.sysagua.service.CashierService;
import com.api.sysagua.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.math.BigDecimal;

@Service
public class CashierServiceImpl implements CashierService {

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionRepository transactionRepository;

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
    public ViewCashierDto list() {
        List<Transaction> transactions = this.transactionRepository.listByStatus(TransactionStatus.PAID);
        this.cashier.setTransactions(transactions);
        calculateBalance();
        return cashier.toView();
    }

    private void createAddBalanceTransaction(BigDecimal balance) {
        var t = new Transaction(
                TransactionStatus.PAID,
                balance,
                TransactionType.INCOME,
                PaymentMethod.UNDEFINED,
                "Incremento de saldo por usuário",
                null,
                null
        );
        this.transactionRepository.save(t);
    }

    private void calculateBalance() {
        BigDecimal balance = BigDecimal.ZERO;

        for (Transaction transaction : cashier.getTransactions()) {
            if (transaction.getStatus() == TransactionStatus.PAID) {
                balance = transaction.getType() == TransactionType.INCOME
                        ? balance.add(transaction.getAmount())
                        : balance.subtract(transaction.getAmount());
            }
        }

        this.cashier.setBalance(balance);
    }

}
