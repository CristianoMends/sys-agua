package com.api.sysagua.controller;

import com.api.sysagua.docs.TransactionDoc;
import com.api.sysagua.dto.transaction.ViewTransactionDto;
import com.api.sysagua.enumeration.TransactionStatus;
import com.api.sysagua.enumeration.TransactionType;
import com.api.sysagua.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("transactions")
public class TransactionController implements TransactionDoc {

    @Autowired
    private TransactionService service;

    @GetMapping
    @CrossOrigin
    public ResponseEntity<List<ViewTransactionDto>> listTransactions(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) TransactionStatus status,
            @RequestParam(required = false) BigDecimal amountStart,
            @RequestParam(required = false) BigDecimal amountEnd,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) LocalDateTime createdAtStart,
            @RequestParam(required = false) LocalDateTime createdAtEnd,
            @RequestParam(required = false) Long orderId,
            @RequestParam(required = false) Long purchaseId
    ) {

        List<ViewTransactionDto> transactions = service.list(
                id,
                status,
                amountStart,
                amountEnd,
                type,
                description,
                createdAtStart,
                createdAtEnd,
                orderId,
                purchaseId
        );
        return ResponseEntity.ok(transactions);
    }
}
