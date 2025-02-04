package com.api.sysagua.service;


import com.api.sysagua.dto.transaction.ViewTransactionDto;
import com.api.sysagua.enumeration.PaymentMethod;
import com.api.sysagua.enumeration.TransactionStatus;
import com.api.sysagua.enumeration.TransactionType;
import com.api.sysagua.model.Transaction;

import java.time.LocalDateTime;
import java.util.List;

import java.math.BigDecimal;

public interface TransactionService {

    void save(Transaction transaction);

    void cancel(Long id);

    void finish(Long id);

    List<ViewTransactionDto> list(
            Long id,
            TransactionStatus status,
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
