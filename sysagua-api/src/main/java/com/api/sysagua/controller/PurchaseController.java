package com.api.sysagua.controller;

import com.api.sysagua.docs.PurchaseDoc;
import com.api.sysagua.dto.purchase.*;
import com.api.sysagua.enumeration.PaymentMethod;
import com.api.sysagua.enumeration.PaymentStatus;
import com.api.sysagua.service.PurchaseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("purchases")
public class PurchaseController implements PurchaseDoc {

    @Autowired
    private PurchaseService purchaseService;

    @PostMapping()
    public ResponseEntity<Object> create(@RequestBody CreatePurchaseDto dto) {
        this.purchaseService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<ViewPurchaseDto>> list(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) BigDecimal totalAmountStart,
            @RequestParam(required = false) BigDecimal totalAmountEnd,
            @RequestParam(required = false) BigDecimal paidAmountStart,
            @RequestParam(required = false) BigDecimal paidAmountEnd,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) LocalDateTime entryAtStart,
            @RequestParam(required = false) LocalDateTime entryAtEnd,
            @RequestParam(required = false) LocalDateTime createdAtStart,
            @RequestParam(required = false) LocalDateTime createdAtEnd,
            @RequestParam(required = false) LocalDateTime finishedAtStart,
            @RequestParam(required = false) LocalDateTime finishedAtEnd,
            @RequestParam(required = false) LocalDateTime canceledAtStart,
            @RequestParam(required = false) LocalDateTime canceledAtEnd,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Long supplierId,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) String nfe,
            @RequestParam(required = false) PaymentMethod paymentMethod,
            @RequestParam(required = false) PaymentStatus paymentStatus
    ) {
        return ResponseEntity.ok(this.purchaseService.list(
                id,
                totalAmountStart,
                totalAmountEnd,
                paidAmountStart,
                paidAmountEnd,
                active,
                entryAtStart,
                entryAtEnd,
                createdAtStart,
                createdAtEnd,
                finishedAtStart,
                finishedAtEnd,
                canceledAtStart,
                canceledAtEnd,
                description,
                supplierId,
                productId,
                nfe,
                paymentMethod,
                paymentStatus
        ));
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> update(
            @PathVariable Long id,
            @RequestBody @Valid UpdatePurchaseDto update
    ) {
        this.purchaseService.update(id, update);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.purchaseService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
