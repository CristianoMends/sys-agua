package com.api.sysagua.controller;

import com.api.sysagua.docs.PurchaseDoc;
import com.api.sysagua.dto.purchase.*;
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
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "totalValueStart", required = false) BigDecimal totalValueStart,
            @RequestParam(value = "totalValueEnd", required = false) BigDecimal totalValueEnd,
            @RequestParam(value = "active", required = false) Boolean active,
            @RequestParam(value = "updatedAtStart", required = false) LocalDateTime updatedAtStart,
            @RequestParam(value = "updatedAtEnd", required = false) LocalDateTime updatedAtEnd,
            @RequestParam(value = "createdAtStart", required = false) LocalDateTime createdAtStart,
            @RequestParam(value = "createdAtEnd", required = false) LocalDateTime createdAtEnd,
            @RequestParam(value = "supplierId", required = false) Long supplierId,
            @RequestParam(value = "productId", required = false) Long productId,
            @RequestParam(required = false) LocalDateTime finishedAtStart,
            @RequestParam(required = false) LocalDateTime finishedAtEnd,
            @RequestParam(required = false) LocalDateTime canceledAtStart,
            @RequestParam(required = false) LocalDateTime canceledAtEnd,
            @RequestParam(required = false) String description
    ) {
        return ResponseEntity.ok(this.purchaseService.list(
                id,
                totalValueStart,
                totalValueEnd,
                active,
                updatedAtStart,
                updatedAtEnd,
                createdAtStart,
                createdAtEnd,
                supplierId,
                productId,
                finishedAtStart,
                finishedAtEnd,
                canceledAtStart,
                canceledAtEnd,
                description
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
