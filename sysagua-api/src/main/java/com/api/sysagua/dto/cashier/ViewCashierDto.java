package com.api.sysagua.dto.cashier;

import com.api.sysagua.dto.transaction.ViewTransactionDto;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ViewCashierDto {

    @Schema(description = "Saldo atual do caixa", example = "1500.75")
    private BigDecimal balance;

    @ArraySchema(
            schema = @Schema(implementation = ViewTransactionDto.class)
    )
    private List<ViewTransactionDto> transactions;
}
