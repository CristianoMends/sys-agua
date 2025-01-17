package com.api.sysagua.docs;

import com.api.sysagua.dto.product.CreateProductDto;
import com.api.sysagua.dto.product.UpdateProductDto;
import com.api.sysagua.dto.product.ViewProductDto;
import com.api.sysagua.exception.ResponseError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface ProductDoc {

    @Operation(
            summary = "Registrar um novo produto",
            description = "Registra um novo produto no sistema com as informações fornecidas.",
            security = @SecurityRequirement(name = "Bearer")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Produto registrado com sucesso",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados do produto fornecidos são inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Usuario não está autorizado",
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
    ResponseEntity<Void> create(CreateProductDto productDto);

    @Operation(
            summary = "Buscar todos os produtos",
            description = "Retorna uma lista de todos os produtos cadastrados.",
            security = @SecurityRequirement(name = "Bearer")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Produtos encontrados com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ViewProductDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Falha na requisição",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Usuario não está autorizado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseError.class)
                    )
            )
    })
    ResponseEntity<List<ViewProductDto>> list(
            Long id,
            String name,
            String category,
            String unit,
            String brand,
            LocalDate startUpdateDate,
            LocalDate endUpdateDate,
            LocalDate startRegisterDate,
            LocalDate endRegisterDate,
            Boolean active
    );

    @Operation(
            summary = "Atualizar informações de um produto",
            description = "Atualiza as informações de um produto existente com base no ID fornecido.",
            security = @SecurityRequirement(name = "Bearer")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Produto atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json"
                    )
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
                    description = "Usuario não autorizado",
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
            Long id, UpdateProductDto productDto
    );

    @Operation(
            summary = "Deletar um produto",
            description = "Deleta um produto do sistema com base no ID fornecido.",
            security = @SecurityRequirement(name = "Bearer")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Produto deletado com sucesso",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "ID do produto fornecido é inválido",
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
    ResponseEntity<Void> delete(Long id);

}
