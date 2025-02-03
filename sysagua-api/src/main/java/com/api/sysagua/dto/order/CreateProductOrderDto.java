package com.api.sysagua.dto.order;

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
@Schema(description = "DTO para criação de um novo produto pedido.")
public class CreateProductOrderDto {

    @Schema(description = "Identificador do produto", example = "1")
    private Long productId;

    @Schema(description = "Quantidade do produto solicitada", example = "3")
    private Integer quantity;

    @Schema(description = "Preço unitário de venda do produto", example = "19.99")
    private BigDecimal unitPrice;

}
