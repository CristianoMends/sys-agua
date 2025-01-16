package com.api.sysagua.dto.stock;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Schema(description = "DTO com dados para adicionar produtos ao estoque")
public class AddProductDto {

    @Schema(description = "Identificador único da produtoCompra", example = "1")
    private Long productId;

    @Schema(description = "Valor de custo do produto", example = "47.59")
    private Double cost;

    @Schema(description = "Preço de venda do produto ao ser adicionado ao estoque", example = "50.00")
    private Double price;

    @Schema(description = "Quantidade de produtos a ser adicionado ao estoque", example = "25")
    private Integer quantity;

}
