package com.api.sysagua.dto.stock;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchStockDto {
    @Schema(description = "ID do estoque", example = "1")
    private Long id;

    @Schema(description = "Quantidade mínima do estoque", example = "10")
    private Integer initialQuantityStart;

    @Schema(description = "Quantidade máxima do estoque", example = "100")
    private Integer initialQuantityEnd;

    @Schema(description = "Quantidade de entradas mínima", example = "5")
    private Integer totalEntriesStart;

    @Schema(description = "Quantidade de entradas máxima", example = "50")
    private Integer totalEntriesEnd;

    @Schema(description = "Quantidade de saídas mínima", example = "5")
    private Integer totalWithdrawalsStart;

    @Schema(description = "Quantidade de saídas máxima", example = "50")
    private Integer totalWithdrawalsEnd;

    @Schema(description = "Data de adição inicial do estoque", example = "2023-01-01T00:00:00")
    private LocalDateTime createdAtStart;

    @Schema(description = "Data de adição final do estoque", example = "2023-12-31T23:59:59")
    private LocalDateTime createdAtEnd;

    @Schema(description = "Data de atualização inicial do estoque", example = "2023-01-01T00:00:00")
    private LocalDateTime updatedAtStart;

    @Schema(description = "Data de atualização final do estoque", example = "2023-12-31T23:59:59")
    private LocalDateTime updatedAtEnd;

    @Schema(description = "ID do produto", example = "2")
    private Long productId;

    @Schema(description = "Nome do produto", example = "Garrafa de Água")
    private String productName;
}
