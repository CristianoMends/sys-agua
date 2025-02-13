package com.api.sysagua.controller;

import com.api.sysagua.dto.cashier.ViewCashierDto;
import com.api.sysagua.enumeration.TransactionStatus;
import com.api.sysagua.enumeration.TransactionType;
import com.api.sysagua.service.CashierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("cashier")
public class CashierController {
    @Autowired
    private CashierService cashierService;

    @PostMapping("{balance}")
    public ResponseEntity<Void> addBalance(@PathVariable BigDecimal balance){
        this.cashierService.addBalance(balance);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<ViewCashierDto> list(
            @RequestParam(required = false) Long transactionId,
            @RequestParam(required = false) TransactionStatus transactionStatus,
            @RequestParam(required = false) BigDecimal amountStart,
            @RequestParam(required = false) BigDecimal amountEnd,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) LocalDateTime createdAtStart,
            @RequestParam(required = false) LocalDateTime createdAtEnd,
            @RequestParam(required = false) UUID responsibleUserId,
            @RequestParam(required = false) Long orderId,
            @RequestParam(required = false) Long purchaseId
    ) {
        var list = this.cashierService.list(
                transactionId,
                transactionStatus,
                amountStart,
                amountEnd,
                type,
                description,
                createdAtStart,
                createdAtEnd,
                responsibleUserId,
                orderId,
                purchaseId
        );
        return ResponseEntity.ok(list);
    }

}
