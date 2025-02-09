package com.api.sysagua.model;

import com.api.sysagua.dto.cashier.ViewCashierDto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Cashier {
    private BigDecimal balance;

    private List<Transaction> transactions;

    public Cashier(BigDecimal initialBalance) {
        this.balance = initialBalance;
        this.transactions = new ArrayList<>();
    }

    public ViewCashierDto toView() {
        return new ViewCashierDto(
                getBalance(),
                getTransactions().stream().map(Transaction::toView).toList()
        );
    }
}
