package com.api.sysagua.service;

import com.api.sysagua.dto.cashier.ViewCashierDto;

import java.math.BigDecimal;

public interface CashierService {

    void addBalance(BigDecimal balance);
    ViewCashierDto list();

}
