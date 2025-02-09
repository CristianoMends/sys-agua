package com.api.sysagua.service;

import com.api.sysagua.dto.cashier.ViewCashierDto;
import com.api.sysagua.enumeration.TransactionStatus;
import com.api.sysagua.enumeration.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface CashierService {

    void addBalance(BigDecimal balance);
    ViewCashierDto list(
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
    );

}
