package com.api.sysagua.controller;

import com.api.sysagua.docs.ProductCategoryDoc;
import com.api.sysagua.dto.product.CreateCategoryDto;
import com.api.sysagua.model.ProductCategory;
import com.api.sysagua.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("products/category")
public class ProductCategoryController implements ProductCategoryDoc {

    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping
    public ResponseEntity<List<ProductCategory>> list(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean active
    ){

        return ResponseEntity.ok(this.productCategoryService.list(id,name, active));
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CreateCategoryDto dto){
        this.productCategoryService.create(dto.getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        this.productCategoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
