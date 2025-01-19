package com.api.sysagua.controller;

import com.api.sysagua.docs.PurchaseDoc;
import com.api.sysagua.dto.purchase.*;
import com.api.sysagua.service.PurchaseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("purchases")
public class PurchaseController implements PurchaseDoc {

    @Autowired
    private PurchaseService purchaseService;

    @PostMapping()
    public ResponseEntity<Object> create(@RequestBody CreatePurchaseDto dto) {
        this.purchaseService.create(dto);
        return ResponseEntity.noContent().build();
    }


    @GetMapping
    public ResponseEntity<List<ViewPurchaseDto>> list(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "totalValueStart", required = false) Double totalValueStart,
            @RequestParam(value = "totalValueEnd", required = false) Double totalValueEnd,
            @RequestParam(value = "active", required = false) Boolean active,
            @RequestParam(value = "updatedAtStart", required = false) ZonedDateTime updatedAtStart,
            @RequestParam(value = "updatedAtEnd", required = false) ZonedDateTime updatedAtEnd,
            @RequestParam(value = "createdAtStart", required = false) ZonedDateTime createdAtStart,
            @RequestParam(value = "createdAtEnd", required = false) ZonedDateTime createdAtEnd,
            @RequestParam(value = "supplierId", required = false) Long supplierId,
            @RequestParam(value = "productId", required = false) Long productId
    ) {
        var search = new SearchPurchaseDto(
                id,
                totalValueStart,
                totalValueEnd,
                active,
                updatedAtStart,
                updatedAtEnd,
                createdAtStart,
                createdAtEnd,
                supplierId,
                productId
        );
        return ResponseEntity.ok(this.purchaseService.list(search));
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
    public ResponseEntity<Void> delete(@PathVariable Long id){
        this.purchaseService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
