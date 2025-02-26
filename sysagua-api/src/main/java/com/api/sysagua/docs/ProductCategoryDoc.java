package com.api.sysagua.docs;

import com.api.sysagua.dto.product.CreateCategoryDto;
import com.api.sysagua.model.ProductCategory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Product Category Controller", description = "Controlador responsável pela gestão das categorias de produtos.")
public interface ProductCategoryDoc {

    @Operation(summary = "Lista as categorias de produtos",
            description = "Retorna uma lista de categorias de produtos com base nos filtros fornecidos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de categorias de produtos retornada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos."),
            @ApiResponse(responseCode = "403", description = "Acesso proibido."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })

    ResponseEntity<List<ProductCategory>> list(
            @Parameter(description = "Identificador da categoria de produto") Long id,
            @Parameter(description = "Nome da categoria de produto") String name,
            @Parameter(description = "Status ativo da categoria de produto") Boolean active);

    @Operation(summary = "Cria uma nova categoria de produto",
            description = "Recebe os dados necessários para criar uma nova categoria de produto.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Categoria de produto criada com sucesso."),
            @ApiResponse(responseCode = "403", description = "Acesso proibido."),
    })

    ResponseEntity<Void> create(
            @RequestBody @Parameter(description = "Dados para criar uma nova categoria de produto") CreateCategoryDto dto);

    @Operation(summary = "Deleta uma categoria de produto",
            description = "Remove uma categoria de produto do sistema com base no ID fornecido.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Categoria de produto deletada com sucesso."),
            @ApiResponse(responseCode = "403", description = "Acesso proibido."),
            @ApiResponse(responseCode = "404", description = "Categoria de produto não encontrada."),
    })


    ResponseEntity<Void> delete(
            @Parameter(description = "Identificador único da categoria de produto") Long id);
}
