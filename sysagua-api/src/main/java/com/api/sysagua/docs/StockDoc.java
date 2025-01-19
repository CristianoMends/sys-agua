package com.api.sysagua.docs;

import com.api.sysagua.dto.stock.AddProductDto;
import com.api.sysagua.dto.stock.SearchStockDto;
import com.api.sysagua.exception.ResponseError;
import com.api.sysagua.model.Stock;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

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
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Produto adicionado ao estoque com sucesso."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação nos dados fornecidos.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseError.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno no servidor.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseError.class)
                            )
                    )
            }
    )
    ResponseEntity<Void> addProduct(AddProductDto dto);

    @Operation(
            summary = "Obtém produtos do estoque com filtros",
            description = "Este endpoint retorna uma lista de produtos no estoque com base nos filtros fornecidos.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de produtos no estoque retornada com sucesso.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Stock.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno no servidor.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseError.class)
                            )
                    )
            }
    )
    @GetMapping
    ResponseEntity<List<Stock>> list(
            Long id,
            Integer quantityStart,
            Integer quantityEnd,
            Integer exitsStart,
            Integer exitsEnd,
            LocalDate addedAtStart,
            LocalDate addedAtEnd,
            Integer entriesStart,
            Integer entriesEnd,
            Long productId
    );

    @Operation(
            summary = "Atualizar informações do estoque de um produto",
            description = "Atualiza informações relacionadas ao estoque de um produto, como preço, custo, quantidade, entradas e saídas, com base no ID do produto fornecido.",
            security = @SecurityRequirement(name = "Bearer")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Estoque do produto atualizado com sucesso",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados fornecidos são inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Produto não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Usuário não autorizado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro inesperado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseError.class)
                    )
            )
    })
    ResponseEntity<Void> update(
            Long productId,
            Double price,
            Double cost,
            Integer quantity,
            Integer entries,
            Integer exits
    );


}
