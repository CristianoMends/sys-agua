package com.api.sysagua.dto.order;

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
public class ViewProductOrderDto {
    @Schema(description = "Identificador do produto pedido", example = "1")
    private Long id;

    @Schema(description = "Identificador do pedido ao qual o produto pertence", example = "10")
    private Long orderId;

    @Schema(description = "Quantidade do produto solicitada", example = "5")
    private Integer quantity;

    @Schema(description = "Preço unitário do produto", example = "19.99")
    private BigDecimal unitPrice;

    @Schema(description = "Preço total calculado (unitPrice x quantity)", example = "99.95")
    private BigDecimal totalPrice;

    @Schema(description = "Detalhes do produto", implementation = ViewProductDto.class)
    private ViewProductDto product;
}
