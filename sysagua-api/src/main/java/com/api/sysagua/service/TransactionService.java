package com.api.sysagua.service;


import com.api.sysagua.enumeration.PaymentMethod;
import com.api.sysagua.enumeration.StatusTransaction;
import com.api.sysagua.enumeration.TransactionType;
import com.api.sysagua.model.Transaction;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

import java.math.BigDecimal;

public interface TransactionService {

    void save(BigDecimal amount, TransactionType type, PaymentMethod paymentMethod, String description);

    void cancel(Long id);

    void finish(Long id);

    List<Transaction> list(
            Long id,
            StatusTransaction status,
            BigDecimal amountStart,
            BigDecimal amountEnd,
            TransactionType type,
            PaymentMethod paymentMethod,
            String description,
            LocalDateTime createdAtStart,
            LocalDateTime createdAtEnd,
            LocalDateTime finishedAtStart,
            LocalDateTime finishedAtEnd,
            LocalDateTime canceledAtStart,
            LocalDateTime canceledAtEnd
    );

}
