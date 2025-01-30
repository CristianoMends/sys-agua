package com.api.sysagua.dto.purchase;

import com.api.sysagua.dto.product.ViewProductDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para visualização de uma compra de produto")
public class ViewProductPurchaseDto {

    @Schema(description = "Identificador único", example = "1")
    private Long id;

    @Schema(description = "Detalhes do produto adquirido")
    private ViewProductDto product;

    @Schema(description = "Quantidade do produto comprado", example = "5")
    private Integer quantity;

    @Schema(description = "Preço de compra do produto por unidade", example = "15.99")
    private BigDecimal purchasePrice;

    @Schema(description = "Preço total da compra do produto (quantidade * preço de compra)", example = "79.95")
    private BigDecimal total;
}
