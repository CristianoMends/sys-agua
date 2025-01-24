package com.api.sysagua.dto.purchase;

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
@Schema(description = "DTO para pesquisa de compras")
public class SearchPurchaseDto {

    @Schema(description = "Identificador único da compra", example = "1")
    private Long id;

    @Schema(description = "Valor mínimo total da compra", example = "100.00")
    private Double totalValueStart;

    @Schema(description = "Valor máximo total da compra", example = "500.00")
    private Double totalValueEnd;

    @Schema(description = "Status ativo da compra (true ou false)", example = "true")
    private Boolean active;

    @Schema(description = "Data inicial de atualização da compra", example = "2025-01-01T00:00:00Z")
    private LocalDateTime updatedAtStart;

    @Schema(description = "Data final de atualização da compra", example = "2025-01-15T23:59:59Z")
    private LocalDateTime updatedAtEnd;

    @Schema(description = "Data inicial de criação da compra", example = "2025-01-01T00:00:00Z")
    private LocalDateTime createdAtStart;

    @Schema(description = "Data final de criação da compra", example = "2025-01-15T23:59:59Z")
    private LocalDateTime createdAtEnd;

    @Schema(description = "Identificador único do fornecedor associado à compra", example = "3")
    private Long supplierId;

    @Schema(description = "Identificador único do produto associado à compra", example = "5")
    private Long productId;
}
