package com.api.sysagua.controller;

import com.api.sysagua.docs.ProductDoc;
import com.api.sysagua.dto.product.CreateProductDto;
import com.api.sysagua.dto.product.SearchProductDto;
import com.api.sysagua.dto.product.UpdateProductDto;
import com.api.sysagua.dto.product.ViewProductDto;
import com.api.sysagua.model.Product;
import com.api.sysagua.service.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/products")
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Product Controller", description = "Endpoints para gerenciamento de produtos")
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
    public ResponseEntity<List<ViewProductDto>> list(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String unit,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) LocalDate startUpdateDate,
            @RequestParam(required = false) LocalDate endUpdateDate,
            @RequestParam(required = false) LocalDate startRegisterDate,
            @RequestParam(required = false) LocalDate endRegisterDate,
            @RequestParam(required = false) Boolean active
    ) {

        var searchProductDto = new SearchProductDto(
                id,
                name,
                unit,
                brand,
                category,
                startUpdateDate,
                endUpdateDate,
                startRegisterDate,
                endRegisterDate,
                active

        );

        List<ViewProductDto> products = productService.getProducts(searchProductDto)
                .stream()
                .map(Product::toView)
                .toList();

        return ResponseEntity.ok().body(products);
    }

    @PutMapping()
    @CrossOrigin
    public ResponseEntity<Void> update(
            @RequestParam Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String unit,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean active
    ) {
        UpdateProductDto productDto = new UpdateProductDto(name, unit,brand, category, active);
        this.productService.updateProduct(id, productDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    @Override
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ){
      this.productService.delete(id);
      return ResponseEntity.noContent().build();
    }

}
