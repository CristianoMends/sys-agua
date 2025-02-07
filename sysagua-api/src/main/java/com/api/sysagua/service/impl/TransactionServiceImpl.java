package com.api.sysagua.service.impl;

import com.api.sysagua.dto.transaction.ViewTransactionDto;
import com.api.sysagua.enumeration.PaymentMethod;
import com.api.sysagua.enumeration.TransactionStatus;
import com.api.sysagua.enumeration.TransactionType;
import com.api.sysagua.model.*;
import com.api.sysagua.repository.StockRepository;
import com.api.sysagua.repository.TransactionRepository;
import com.api.sysagua.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
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
