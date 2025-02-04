package com.api.sysagua.service.impl;

import com.api.sysagua.enumeration.PaymentMethod;
import com.api.sysagua.enumeration.StatusTransaction;
import com.api.sysagua.enumeration.TransactionType;
import com.api.sysagua.exception.BusinessException;
import com.api.sysagua.model.Transaction;
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

    @Override
    public void save(BigDecimal amount, TransactionType type, PaymentMethod paymentMethod, String description) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setStatus(StatusTransaction.PENDING);
        transaction.setDescription(description);
        transaction.setPaymentMethod(paymentMethod);
        transaction.setType(type);
        this.transactionRepository.save(transaction);
    }

    @Override
    public void cancel(Long id) {
        var t = this.transactionRepository.findById(id).orElseThrow(() -> new BusinessException("Transaction not found", HttpStatus.NOT_FOUND));
        t.setCanceledAt(LocalDateTime.now());
        this.transactionRepository.save(t);
    }

    @Override
    public void finish(Long id) {
        var t = this.transactionRepository.findById(id).orElseThrow(() -> new BusinessException("Transaction not found", HttpStatus.NOT_FOUND));
        t.setFinishedAt(LocalDateTime.now());
        this.transactionRepository.save(t);
    }

    @Override
    public List<Transaction> list(Long id, StatusTransaction status, BigDecimal amountStart, BigDecimal amountEnd, TransactionType type, PaymentMethod paymentMethod, String description, LocalDateTime createdAtStart, LocalDateTime createdAtEnd, LocalDateTime finishedAtStart, LocalDateTime finishedAtEnd, LocalDateTime canceledAtStart, LocalDateTime canceledAtEnd) {
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
        );
    }


}
