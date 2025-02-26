package com.api.sysagua.dto.productItem;

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
public class ViewProductItemDto {

    @Schema(description = "Identificador único", example = "1")
    private Long id;

    @Schema(description = "Quantidade do produto comprado", example = "10")
    private Integer quantity;

    @Schema(description = "Preço por unidade", example = "30.00")
    private BigDecimal unitPrice;

    @Schema(description = "Preço total do produto (quantidade * preço)", example = "300.00")
    private BigDecimal total;

    @Schema(description = "Detalhes do produto")
    private ViewProductDto product;
}
