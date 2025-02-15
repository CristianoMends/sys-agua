package com.api.sysagua.dto.purchase;

import com.api.sysagua.enumeration.PaymentMethod;
import com.api.sysagua.enumeration.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePurchaseDto {

    @Schema(description = "Identificador único do fornecedor associado à compra", example = "2")
    private Long supplierId;

    @Schema(description = "Descrição da compra", example = "Sem descrição")
    private String description;

    @Schema(description = "Quantia já paga do valor da compra", example = "249.99")
    private BigDecimal paidAmount;

    @Schema(description = "Quantia total da compra", example = "549.99")
    private BigDecimal totalAmount;

    @Schema(description = "metodo de pagamento", example = "PIX")
    private PaymentMethod paymentMethod;

    @Schema(description = "status de pagamento, caso não infomado, é adicionado de acordo com o valor total e já pago", example = "PENDING")
    private PaymentStatus paymentStatus;

    @Schema(description = "Data e hora da entrada da compra", example = "2025-01-14T10:00:00Z")
    private LocalDateTime entryAt;

    @Schema(description = "Número nota fiscal eletrônica", example = "123456789")
    @Pattern(regexp = "\\d+", message = "The nfe must contain only numbers")
    private String nfe;

}
