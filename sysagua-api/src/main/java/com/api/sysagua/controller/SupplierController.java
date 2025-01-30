package com.api.sysagua.controller;

import com.api.sysagua.docs.SupplierDoc;
import com.api.sysagua.dto.supplier.CreateSupplierDto;
import com.api.sysagua.dto.supplier.UpdateSupplierDto;
import com.api.sysagua.model.Supplier;
import com.api.sysagua.service.SupplierService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("suppliers")
public class SupplierController implements SupplierDoc {
    @Autowired
    private SupplierService service;

    @PostMapping
    @CrossOrigin
    public ResponseEntity<Void> create(@RequestBody @Valid CreateSupplierDto dto){
        service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    @CrossOrigin
    public ResponseEntity<List<Supplier>> list(
            @RequestParam(value = "id",required = false) Long id,
            @RequestParam(value = "socialReason",required = false) String socialReason,
            @RequestParam(value = "cnpj", required = false) String cnpj,
            @RequestParam(value = "phone",required = false) String phone,
            @RequestParam(value = "active", required = false) Boolean active
    ){
        return ResponseEntity.ok(this.service.list(id,socialReason,cnpj,phone, active));
    }

    @DeleteMapping("{id}")
    @CrossOrigin
    public ResponseEntity<Void> delete(@PathVariable Long id){
        this.service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    @CrossOrigin
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid UpdateSupplierDto dto){
        this.service.update(id,dto);
        return ResponseEntity.noContent().build();
    }
}
