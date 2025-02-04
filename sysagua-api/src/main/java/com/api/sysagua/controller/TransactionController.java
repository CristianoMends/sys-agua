package com.api.sysagua.controller;

import com.api.sysagua.docs.TransactionDoc;
import com.api.sysagua.enumeration.PaymentMethod;
import com.api.sysagua.enumeration.StatusTransaction;
import com.api.sysagua.enumeration.TransactionType;
import com.api.sysagua.model.Transaction;
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

    @CrossOrigin
    @PutMapping("cancel/{id}")
    public ResponseEntity<Void> cancelTransaction(@PathVariable Long id) {

        service.cancel(id);
        return ResponseEntity.noContent().build();
    }

    @CrossOrigin
    @PutMapping("finish/{id}")
    public ResponseEntity<Void> finishTransaction(@PathVariable Long id) {

        service.finish(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @CrossOrigin
    public ResponseEntity<List<Transaction>> listTransactions(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) StatusTransaction status,
            @RequestParam(required = false) BigDecimal amountStart,
            @RequestParam(required = false) BigDecimal amountEnd,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) PaymentMethod paymentMethod,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) LocalDateTime createdAtStart,
            @RequestParam(required = false) LocalDateTime createdAtEnd,
            @RequestParam(required = false) LocalDateTime finishedAtStart,
            @RequestParam(required = false) LocalDateTime finishedAtEnd,
            @RequestParam(required = false) LocalDateTime canceledAtStart,
            @RequestParam(required = false) LocalDateTime canceledAtEnd
    ) {

        List<Transaction> transactions = service.list(
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
        return ResponseEntity.ok(transactions);
    }
}
