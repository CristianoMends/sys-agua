package com.api.sysagua.dto.purchase;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @NotNull(message = "Supplier ID cannot be null.")
    @Schema(description = "Identificador único do fornecedor", example = "1")
    private Long supplierId;
}
