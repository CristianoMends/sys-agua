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

    @Schema(description = "Quantidade de produtos a ser adicionado ao estoque", example = "25")
    private Integer quantity;

}
