package com.api.sysagua.dto.productItem;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
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
@Schema(description = "DTO para criar um item produto\n")
public class CreateProductItemDto {

    @NotNull(message = "Product ID cannot be null.")
    @Schema(description = "Identificador único do produto", example = "1")
    private Long productId;

    @NotNull(message = "Quantity cannot be null.")
    @Min(value = 1, message = "Quantity must be at least 1.")
    @Schema(description = "Quantidade do produto que está sendo adquirido", example = "10", minimum = "1")
    private Integer quantity;

    @DecimalMin(value = "0.01", message = "Purchase price must be at least 0.01.")
    @Schema(description = "Preço do produto por unidade", example = "30.00", minimum = "0.01")
    private BigDecimal unitPrice;

}
