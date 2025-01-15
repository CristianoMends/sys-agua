package com.api.sysagua.dto.stock;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO utilizado para transferir informações de atualização de estoque.")
public class UpdateStockDto {

    @Schema(description = "Identificador único do produto", example = "1")
    private Long productId;

    @Schema(description = "Novo preço do produto. Pode ser nulo caso não haja atualização do preço",
            example = "29.99")
    private Double price;

    @Schema(description = "Novo custo do produto. Pode ser nulo caso não haja atualização do custo",
            example = "15.50")
    private Double cost;

    @Schema(description = "Nova quantidade em estoque. Pode ser nulo caso não haja alteração na quantidade",
            example = "100")
    private Integer quantity;

    @Schema(description = "Quantidade de entradas no estoque. Pode ser nulo caso não haja alterações",
            example = "10")
    private Integer entries;

    @Schema(description = "Quantidade de saídas do estoque. Pode ser nulo caso não haja alterações",
            example = "5")
    private Integer exits;
}
