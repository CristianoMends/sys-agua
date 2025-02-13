package com.api.sysagua.docs;

import com.api.sysagua.dto.stock.AddProductDto;
import com.api.sysagua.dto.stock.UpdateStockDto;
import com.api.sysagua.exception.ResponseError;
import com.api.sysagua.model.Stock;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Stock Controller", description = "Operações relacionadas ao gerenciamento de estoque.")
public interface StockDoc {

    @Operation(
            summary = "Adiciona um produto ao estoque",
            description = "Este endpoint permite adicionar um produto ao estoque com uma quantidade inicial.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Informações para adicionar um produto ao estoque",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AddProductDto.class)
                    )
            )

    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "sucesso.", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos.", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Não autorizado.", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.", content = @Content())
    })
    ResponseEntity<Void> addProduct(AddProductDto dto);

    @Operation(
            summary = "Obtém produtos do estoque com filtros",
            description = "Este endpoint retorna uma lista de produtos no estoque com base nos filtros fornecidos."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "sucesso.",
                    content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Stock.class)
            )),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos.", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Não autorizado.", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.", content = @Content())
    })
    @GetMapping
    ResponseEntity<List<Stock>> list(
            Long id,
            Integer initialQuantityStart,
            Integer initialQuantityEnd,
            Integer totalEntriesStart,
            Integer totalEntriesEnd,
            Integer totalWithdrawalsStart,
            Integer totalWithdrawalsEnd,
            LocalDateTime createdAtStart,
            LocalDateTime createdAtEnd,
            LocalDateTime updatedAtStart,
            LocalDateTime updatedAtEnd,
            Long productId,
            String productName
    );

    @Operation(
            summary = "Atualizar informações do estoque de um produto",
            description = "Atualiza informações relacionadas ao estoque de um produto, como preço, custo, quantidade, entradas e saídas, com base no ID do produto fornecido.",
            security = @SecurityRequirement(name = "Bearer")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "sucesso.", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos.", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Não autorizado.", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.", content = @Content())
    })
    ResponseEntity<Void> update(
            Long id,
            UpdateStockDto update
    );


}
