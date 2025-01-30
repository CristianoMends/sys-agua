package com.api.sysagua.controller;

import com.api.sysagua.docs.ProductDoc;
import com.api.sysagua.dto.product.CreateProductDto;
import com.api.sysagua.dto.product.UpdateProductDto;
import com.api.sysagua.model.Product;
import com.api.sysagua.service.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController implements ProductDoc {

    @Autowired
    private ProductService productService;

    @CrossOrigin
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid CreateProductDto productDto) {
        productService.registerProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    @CrossOrigin
    public ResponseEntity<List<Product>> list(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal priceStart,
            @RequestParam(required = false) BigDecimal priceEnd,
            @RequestParam(required = false) BigDecimal costStart,
            @RequestParam(required = false) BigDecimal costEnd,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String unit,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) LocalDateTime startUpdateDate,
            @RequestParam(required = false) LocalDateTime endUpdateDate,
            @RequestParam(required = false) LocalDateTime startRegisterDate,
            @RequestParam(required = false) LocalDateTime endRegisterDate,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) String line,
            @RequestParam(required = false) String ncm
    ) {
        List<Product> products = productService.getProducts(
                        id,
                        name,
                        priceStart,
                        priceEnd,
                        costStart,
                        costEnd,
                        unit,
                        brand,
                        category,
                        startRegisterDate,
                        endRegisterDate,
                        startUpdateDate,
                        endUpdateDate,
                        active,
                        ncm,
                        line)
                .stream()
                .toList();

        return ResponseEntity.ok().body(products);
    }

    @PutMapping("{id}")
    @CrossOrigin
    public ResponseEntity<Void> update(
            @PathVariable Long id,
            @RequestBody UpdateProductDto productDto
    ) {
        this.productService.updateProduct(id, productDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    @Override
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        this.productService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
