package com.api.sysagua.docs;

import com.api.sysagua.dto.stockHistory.ViewStockHistoryDto;
import com.api.sysagua.enumeration.MovementType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface StockHistoryDoc {

    @Operation(
            summary = "Listar registros do histórico de estoque",
            description = "Recupera uma lista de movimentações de estoque com base em critérios de filtro opcionais."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "sucesso."),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos.", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Não autorizado.", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.", content = @Content())
    })
    @GetMapping
    ResponseEntity<List<ViewStockHistoryDto>> list(
            @Parameter(description = "ID do registro do histórico de estoque") @RequestParam(required = false) Long id,
            @Parameter(description = "Tipo de movimentação de estoque") @RequestParam(required = false) MovementType tipo,
            @Parameter(description = "Quantidade mínima para filtro") @RequestParam(required = false) Integer quantidadeMinima,
            @Parameter(description = "Quantidade máxima para filtro") @RequestParam(required = false) Integer quantidadeMaxima,
            @Parameter(description = "Data inicial para filtro") @RequestParam(required = false) LocalDateTime dataInicial,
            @Parameter(description = "Data final para filtro") @RequestParam(required = false) LocalDateTime dataFinal,
            @Parameter(description = "Descrição da movimentação de estoque") @RequestParam(required = false) String descricao,
            @Parameter(description = "ID do usuário responsável pela movimentação") @RequestParam(required = false) UUID usuarioResponsavelId,
            @Parameter(description = "ID do estoque relacionado à movimentação") @RequestParam(required = false) Long estoqueId
    );
}
