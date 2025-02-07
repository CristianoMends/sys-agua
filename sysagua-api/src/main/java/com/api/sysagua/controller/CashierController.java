package com.api.sysagua.controller;

import com.api.sysagua.dto.cashier.ViewCashierDto;
import com.api.sysagua.service.CashierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("cashier")
public class CashierController {
    @Autowired
    private CashierService cashierService;

    @PostMapping("{balance}")
    public ResponseEntity<Void> addBalance(@PathVariable BigDecimal balance){
        this.cashierService.addBalance(balance);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<ViewCashierDto> list() {
        var list = this.cashierService.list();
        return ResponseEntity.ok(list);
    }

}
