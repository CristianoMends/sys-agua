package com.api.sysagua.controller;

import com.api.sysagua.docs.ProductLineDoc;
import com.api.sysagua.dto.product.CreateLineDto;
import com.api.sysagua.model.ProductLine;
import com.api.sysagua.service.ProductLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("products/line")
public class ProductLineController implements ProductLineDoc {

    @Autowired
    private ProductLineService productLineService;

    @GetMapping
    public ResponseEntity<List<ProductLine>> list(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean active
    ){

        return ResponseEntity.ok(this.productLineService.list(id,name, active));
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CreateLineDto dto){
        this.productLineService.create(dto.getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        this.productLineService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
