package com.api.sysagua.dto.stock;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchStockDto {
    @Schema(description = "ID do estoque", example = "1")
    private Long id;

    @Schema(description = "Quantidade mínima do estoque", example = "10")
    private Integer quantityStart;

    @Schema(description = "Quantidade máxima do estoque", example = "100")
    private Integer quantityEnd;

    @Schema(description = "Quantidade de saídas mínima", example = "5")
    private Integer exitsStart;

    @Schema(description = "Quantidade de saídas máxima", example = "50")
    private Integer exitsEnd;

    @Schema(description = "Data de adição inicial do estoque", example = "2023-01-01")
    private LocalDate addedAtStart;

    @Schema(description = "Data de adição final do estoque", example = "2023-12-31")
    private LocalDate addedAtEnd;

    @Schema(description = "Quantidade de entradas mínima", example = "5")
    private Integer entriesStart;

    @Schema(description = "Quantidade de entradas máxima", example = "50")
    private Integer entriesEnd;

    @Schema(description = "Id do produto", example = "2")
    private Long productId;
}
