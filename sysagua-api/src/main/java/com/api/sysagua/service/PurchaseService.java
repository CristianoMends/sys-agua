package com.api.sysagua.service;

import com.api.sysagua.dto.purchase.CreatePurchaseDto;
import com.api.sysagua.dto.purchase.UpdatePurchaseDto;
import com.api.sysagua.dto.purchase.ViewPurchaseDto;
import com.api.sysagua.dto.transaction.CreateTransactionDto;
import com.api.sysagua.enumeration.PaymentMethod;
import com.api.sysagua.enumeration.PaymentStatus;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface PurchaseService {

    void create(CreatePurchaseDto dto);

    List<ViewPurchaseDto> list(
            Long id,
            BigDecimal totalAmountStart,
            BigDecimal totalAmountEnd,
            BigDecimal paidAmountStart,
            BigDecimal paidAmountEnd,
            Boolean active,
            LocalDateTime entryAtStart,
            LocalDateTime entryAtEnd,
            LocalDateTime createdAtStart,
            LocalDateTime createdAtEnd,
            LocalDateTime finishedAtStart,
            LocalDateTime finishedAtEnd,
            LocalDateTime canceledAtStart,
            LocalDateTime canceledAtEnd,
            String description,
            Long supplierId,
            Long productId,
            String nfe,
            PaymentMethod paymentMethod,
            PaymentStatus paymentStatus
    );

    void addPayment(Long id, CreateTransactionDto dto);

    void delete(Long id);

}
