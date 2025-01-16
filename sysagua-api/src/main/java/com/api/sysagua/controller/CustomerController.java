package com.api.sysagua.controller;

import com.api.sysagua.docs.CustomerDoc;
import com.api.sysagua.dto.customer.CreateCustomerDto;
import com.api.sysagua.dto.customer.SearchCustomerDto;
import com.api.sysagua.dto.customer.UpdateCustomerDto;
import com.api.sysagua.model.Customer;
import com.api.sysagua.service.CustomerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Customer Controller", description = "Controlador responsável pela gestão dos clientes.")
@RequestMapping("customers")
public class CustomerController implements CustomerDoc {
    @Autowired
    private CustomerService service;

    @CrossOrigin
    @PostMapping
    public ResponseEntity<Void> create(
            @RequestBody @Valid CreateCustomerDto dto
            ){
        this.service.createCustomer(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping
    @CrossOrigin
    public ResponseEntity<List<Customer>> list(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String street,
            @RequestParam(required = false) String neighborhood,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) String cnpj
    ) {
        var search = new SearchCustomerDto(
                id,name,street,neighborhood,city,state,phone, active, cnpj
        );

        List<Customer> customers = service.findByFilters(search);
        return ResponseEntity.ok(customers);
    }

    @CrossOrigin
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        this.service.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid UpdateCustomerDto dto){
        this.service.updateCustomer(id,dto);
        return ResponseEntity.noContent().build();
    }
}
