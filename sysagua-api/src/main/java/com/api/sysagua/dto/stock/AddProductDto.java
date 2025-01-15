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
    private Long productPurchaseId;

    @Schema(description = "Preço de venda do produto ao ser adicionado ao estoque", example = "50.00")
    private Double price;
}
