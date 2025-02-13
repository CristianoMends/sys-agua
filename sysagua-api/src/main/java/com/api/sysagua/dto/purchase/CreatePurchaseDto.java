package com.api.sysagua.dto.purchase;

import com.api.sysagua.enumeration.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para criar uma compra")
public class CreatePurchaseDto {

    @NotNull(message = "Items list cannot be null.")
    @Schema(description = "Lista de produtos com seus dados a serem adquiridos")
    private List<CreateProductPurchaseDto> items;

    @Schema(description = "Quantia já paga do valor da compra", example = "249.99")
    @NotNull
    private BigDecimal paidAmount;

    @Schema(description = "Quantia total da compra, caso não informado, é calculado automaticamente", example = "549.99")
    private BigDecimal totalAmount;

    @NotNull(message = "Supplier ID cannot be null.")
    @Schema(description = "Identificador único do fornecedor", example = "1")
    private Long supplierId;

    @Schema(description = "Descrição da compra", example = "Sem descrição")
    private String description;

    @Schema(description = "metodo de pagamento", example = "PIX")
    @NotNull
    private PaymentMethod paymentMethod;

    @Schema(description = "Data e hora da entrada da compra", example = "2025-01-14T10:00:00Z")
    @NotNull
    private LocalDateTime entryAt;

    @Schema(description = "Número nota fiscal eletrônica", example = "123456789")
    @Pattern(regexp = "\\d+", message = "The nfe must contain only numbers")
    private String nfe;
}
