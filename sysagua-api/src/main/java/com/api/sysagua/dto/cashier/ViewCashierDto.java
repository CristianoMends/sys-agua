package com.api.sysagua.dto.cashier;

import com.api.sysagua.dto.transaction.ViewTransactionDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ViewCashierDto {
    private BigDecimal balance;
    private List<ViewTransactionDto> transactions;
}
