package com.api.sysagua.service;


import com.api.sysagua.dto.transaction.ViewTransactionDto;
import com.api.sysagua.enumeration.TransactionStatus;
import com.api.sysagua.enumeration.TransactionType;

import java.time.LocalDateTime;
import java.util.List;

import java.math.BigDecimal;

public interface TransactionService {

    List<ViewTransactionDto> list(
            Long id,
            TransactionStatus status,
            BigDecimal amountStart,
            BigDecimal amountEnd,
            TransactionType type,
            String description,
            LocalDateTime createdAtStart,
            LocalDateTime createdAtEnd,
            Long orderId,
            Long purchaseId
    );

}
