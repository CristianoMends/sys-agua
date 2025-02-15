package com.api.sysagua.dto.transaction;

import com.api.sysagua.enumeration.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateTransactionDto {

    @Schema(description = "Valor da transação", example = "200.00")
    @NotNull
    @DecimalMin(value = "1.00", message = "amount must be at least 1.00")
    private BigDecimal amount;

    @Schema(description = "Método de pagamento da transação")
    @NotNull
    private PaymentMethod paymentMethod;

    @Schema(description = "Descrição da transação", example = "Pagamento de valor pendente")
    private String description;
}
