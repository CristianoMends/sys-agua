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

import java.time.LocalDate;
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
            @RequestParam(value = "quantityStart", required = false) Integer quantityStart,
            @RequestParam(value = "quantityEnd", required = false) Integer quantityEnd,
            @RequestParam(value = "exitsStart", required = false) Integer exitsStart,
            @RequestParam(value = "exitsEnd", required = false) Integer exitsEnd,
            @RequestParam(value = "addedAtStart", required = false) LocalDate addedAtStart,
            @RequestParam(value = "addedAtEnd", required = false) LocalDate addedAtEnd,
            @RequestParam(value = "entriesStart", required = false) Integer entriesStart,
            @RequestParam(value = "entriesEnd", required = false) Integer entriesEnd,
            @RequestParam(value = "productId", required = false) Long productId
    ) {
        var search = new SearchStockDto(id, quantityStart, quantityEnd, exitsStart, exitsEnd, addedAtStart, addedAtEnd, entriesStart, entriesEnd, productId);
        return ResponseEntity.ok(this.service.getStock(search));
    }

    @PutMapping("{productId}")
    @CrossOrigin
    public ResponseEntity<Void> update(
            @PathVariable Long productId,
            @RequestParam(value = "price",required = false) Double price,
            @RequestParam(value = "const", required = false) Double cost,
            @RequestParam(value = "quantity", required = false) Integer quantity,
            @RequestParam(value = "entries",required = false) Integer entries,
            @RequestParam(value = "exits",required = false) Integer exits
    ) {
        var update = new UpdateStockDto(productId, price,cost,quantity,entries,exits);
        this.service.updateStock(update);
        return ResponseEntity.noContent().build();
    }

}
