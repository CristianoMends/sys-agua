package com.api.sysagua.controller;

import com.api.sysagua.docs.StockDoc;
import com.api.sysagua.dto.stock.AddProductDto;
import com.api.sysagua.dto.stock.SearchStockDto;
import com.api.sysagua.dto.stock.UpdateStockDto;
import com.api.sysagua.model.Stock;
import com.api.sysagua.service.StockService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("stock")
@Tag(name = "Stock Controller", description = "Operações relacionadas ao gerenciamento de estoque.")
public class StockController implements StockDoc {

    @Autowired
    private StockService service;

    @PostMapping
    @CrossOrigin
    public ResponseEntity<Void> addProduct(@RequestBody @Valid AddProductDto dto) {
        this.service.addProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    @CrossOrigin
    public ResponseEntity<List<Stock>> list(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "initialQuantityStart", required = false) Integer initialQuantityStart,
            @RequestParam(value = "initialQuantityEnd", required = false) Integer initialQuantityEnd,
            @RequestParam(value = "totalEntriesStart", required = false) Integer totalEntriesStart,
            @RequestParam(value = "totalEntriesEnd", required = false) Integer totalEntriesEnd,
            @RequestParam(value = "totalWithdrawalsStart", required = false) Integer totalWithdrawalsStart,
            @RequestParam(value = "totalWithdrawalsEnd", required = false) Integer totalWithdrawalsEnd,
            @RequestParam(value = "createdAtStart", required = false) LocalDateTime createdAtStart,
            @RequestParam(value = "createdAtEnd", required = false) LocalDateTime createdAtEnd,
            @RequestParam(value = "updatedAtStart", required = false) LocalDateTime updatedAtStart,
            @RequestParam(value = "updatedAtEnd", required = false) LocalDateTime updatedAtEnd,
            @RequestParam(value = "productId", required = false) Long productId,
            @RequestParam(value = "productName", required = false) String productName
    ) {

        var search = new SearchStockDto(
                id,
                initialQuantityStart,
                initialQuantityEnd,
                totalEntriesStart,
                totalEntriesEnd,
                totalWithdrawalsStart,
                totalWithdrawalsEnd,
                createdAtStart,
                createdAtEnd,
                updatedAtStart,
                updatedAtEnd,
                productId,
                productName
        );

        List<Stock> stockList = this.service.getStock(search);

        return ResponseEntity.ok(stockList);
    }


    public ResponseEntity<Void> update(
            @PathVariable Long id,
            @RequestBody UpdateStockDto update
    ) {
        this.service.updateStock(id,update);
        return ResponseEntity.noContent().build();
    }

}
