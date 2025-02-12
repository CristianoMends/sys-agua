package com.api.sysagua.dto.stockHistory;

import com.api.sysagua.dto.user.ViewUserDto;
import com.api.sysagua.enumeration.MovementType;
import com.api.sysagua.model.Stock;
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
public class ViewStockHistoryDto {

    @Schema(description = "Identificador único do histórico de estoque", example = "1")
    private Long id;

    @Schema(description = "Tipo de movimentação no estoque", example = "ENTRY")
    private MovementType type;

    @Schema(description = "Quantidade movimentada", example = "50")
    private int quantity;

    @Schema(description = "Data e hora da movimentação", example = "2024-02-12T14:30:00")
    private LocalDateTime date;

    @Schema(description = "Descrição da movimentação", example = "Adicionado produto Garrafa de Água 500ml ao estoque")
    private String description;

    @Schema(description = "Usuário responsável pela movimentação", implementation = ViewUserDto.class)
    private ViewUserDto responsibleUser;

    @Schema(description = "Estoque associado à movimentação", implementation = Stock.class)
    private Stock stock;
}
