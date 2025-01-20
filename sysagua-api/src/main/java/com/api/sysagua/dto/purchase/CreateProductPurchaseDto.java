package com.api.sysagua.dto.purchase;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para criar uma compra de produto\n")
public class CreateProductPurchaseDto {

    @NotNull(message = "Product ID cannot be null.")
    @Schema(description = "Identificador único do produto", example = "1")
    private Long productId;

    @NotNull(message = "Quantity cannot be null.")
    @Min(value = 1, message = "Quantity must be at least 1.")
    @Schema(description = "Quantidade do produto que está sendo adquirido", example = "10", minimum = "1")
    private Integer quantity;

    @NotNull(message = "Purchase price cannot be null.")
    @DecimalMin(value = "0.01", message = "Purchase price must be at least 0.01.")
    @Schema(description = "Preço do produto por unidade na compra", example = "29.99", minimum = "0.01")
    private Double purchasePrice;

}
