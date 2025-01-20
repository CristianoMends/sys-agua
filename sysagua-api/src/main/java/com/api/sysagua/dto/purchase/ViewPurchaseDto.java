package com.api.sysagua.dto.purchase;

import com.api.sysagua.model.Supplier;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para visualização de uma compra")
public class ViewPurchaseDto {

    @Schema(description = "Identificador único da compra", example = "1")
    private Long id;

    @Schema(description = "Valor total da compra", example = "1500.00")
    private Double totalValue;

    @Schema(description = "Data e hora da criação da compra", example = "2025-01-14T10:00:00Z")
    private ZonedDateTime createdAt;

    @Schema(description = "Data e hora da criação da compra", example = "2025-01-14T10:00:00Z")
    private ZonedDateTime updatedAt;

    @Schema(description = "Se a compra está ativa no sistema", example = "2025-01-14T10:00:00Z")
    private Boolean active;

    @Schema(description = "Lista de itens comprados com os detalhes do produto",
            implementation = ViewProductPurchaseDto.class)
    private List<ViewProductPurchaseDto> items;

    @Schema(description = "Fornecedor da compra", implementation = Supplier.class)
    private Supplier supplier;
}
