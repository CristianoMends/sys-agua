package com.api.sysagua.docs;


import com.api.sysagua.dto.purchase.CreatePurchaseDto;
import com.api.sysagua.dto.purchase.UpdatePurchaseDto;
import com.api.sysagua.dto.purchase.ViewPurchaseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Purchase Controller", description = "Controlador responsável pela gestão das compras.")
public interface PurchaseDoc {

    @Operation(summary = "Cria uma nova compra",
            description = "Recebe os dados necessários para registrar uma nova compra.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "sucesso.", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos.", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Não autorizado.", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.", content = @Content())
    })
    @PostMapping
    @CrossOrigin
    ResponseEntity<Object> create(
            @RequestBody @Parameter(description = "Dados para criar uma nova compra") CreatePurchaseDto dto);

    @Operation(summary = "Lista compras",
            description = "Retorna uma lista de compras com base nos filtros fornecidos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "sucesso."),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos.", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Não autorizado.", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.", content = @Content())
    })
    @GetMapping
    @CrossOrigin
    ResponseEntity<List<ViewPurchaseDto>> list(
            @Parameter(description = "Identificador da compra") @RequestParam(value = "id", required = false) Long id,
            @Parameter(description = "Valor total mínimo") @RequestParam(value = "totalValueStart", required = false) BigDecimal totalValueStart,
            @Parameter(description = "Valor total máximo") @RequestParam(value = "totalValueEnd", required = false) BigDecimal totalValueEnd,
            @Parameter(description = "Status ativo da compra") @RequestParam(value = "active", required = false) Boolean active,
            @Parameter(description = "Data de atualização inicial") @RequestParam(value = "updatedAtStart", required = false) LocalDateTime updatedAtStart,
            @Parameter(description = "Data de atualização final") @RequestParam(value = "updatedAtEnd", required = false) LocalDateTime updatedAtEnd,
            @Parameter(description = "Data de criação inicial") @RequestParam(value = "createdAtStart", required = false) LocalDateTime createdAtStart,
            @Parameter(description = "Data de criação final") @RequestParam(value = "createdAtEnd", required = false) LocalDateTime createdAtEnd,
            @Parameter(description = "ID do fornecedor") @RequestParam(value = "supplierId", required = false) Long supplierId,
            @Parameter(description = "ID do produto") @RequestParam(value = "productId", required = false) Long productId,
            @Parameter(description = "Data de finalização inicial") LocalDateTime finishedAtStart,
            @Parameter(description = "Data de finalização final") LocalDateTime finishedAtEnd,
            @Parameter(description = "Data de cancelamento inicial") LocalDateTime canceledAtStart,
            @Parameter(description = "Data de cancelamento final") LocalDateTime canceledAtEnd,
            @Parameter(description = "Descrição") String description
    );

    @Operation(summary = "Atualiza uma compra",
            description = "Atualiza os dados de uma compra existente com base no ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "sucesso.", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos.", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Não autorizado.", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.", content = @Content())
    })
    @PutMapping("{id}")
    @CrossOrigin
    ResponseEntity<Void> update(
            @Parameter(description = "Identificador único da compra") @PathVariable Long id,
            @RequestBody @Parameter(description = "Dados para atualizar a compra") @Valid UpdatePurchaseDto update);

    @Operation(summary = "Deleta uma compra",
            description = "Remove uma compra do sistema com base no ID informado.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "sucesso.", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos.", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Não autorizado.", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.", content = @Content())
    })
    @DeleteMapping("{id}")
    @CrossOrigin
    ResponseEntity<Void> delete(
            @Parameter(description = "Identificador único da compra") @PathVariable Long id);
}

