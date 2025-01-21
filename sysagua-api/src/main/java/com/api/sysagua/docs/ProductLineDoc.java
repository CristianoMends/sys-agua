package com.api.sysagua.docs;

import com.api.sysagua.dto.product.CreateLineDto;
import com.api.sysagua.model.ProductLine;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Product Line Controller", description = "Controlador responsável pela gestão das linhas de produtos.")
public interface ProductLineDoc {

    @Operation(summary = "Lista as linhas de produtos",
            description = "Retorna uma lista de linhas de produtos com base nos filtros fornecidos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de linhas de produtos retornada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos."),
            @ApiResponse(responseCode = "403", description = "Acesso proibido."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    ResponseEntity<List<ProductLine>> list(
            @Parameter(description = "Identificador da linha de produto") @RequestParam(required = false) Long id,
            @Parameter(description = "Nome da linha de produto") @RequestParam(required = false) String name,
            @Parameter(description = "Status ativo da linha de produto") @RequestParam(required = false) Boolean active);

    @Operation(summary = "Cria uma nova linha de produto",
            description = "Recebe os dados necessários para criar uma nova linha de produto.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Linha de produto criada com sucesso."),
            @ApiResponse(responseCode = "403", description = "Acesso proibido."),
    })
    ResponseEntity<Void> create(
            @RequestBody @Parameter(description = "Dados para criar uma nova linha de produto") CreateLineDto dto);

    @Operation(summary = "Deleta uma linha de produto",
            description = "Remove uma linha de produto do sistema com base no ID fornecido.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Linha de produto deletada com sucesso."),
            @ApiResponse(responseCode = "403", description = "Acesso proibido."),
            @ApiResponse(responseCode = "404", description = "Linha de produto não encontrada."),
    })
    ResponseEntity<Void> delete(
            @Parameter(description = "Identificador único da linha de produto") Long id);
}
