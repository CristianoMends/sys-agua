package com.api.sysagua.docs;

import com.api.sysagua.dto.product.CreateProductDto;
import com.api.sysagua.dto.product.UpdateProductDto;
import com.api.sysagua.exception.ResponseError;
import com.api.sysagua.model.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Product Controller", description = "Endpoints para gerenciamento de produtos")
public interface ProductDoc {

    @Operation(
            summary = "Registrar um novo produto",
            description = "Registra um novo produto no sistema com as informações fornecidas.",
            security = @SecurityRequirement(name = "Bearer")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "sucesso.", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos.", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Não autorizado.", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.", content = @Content())
    })
    ResponseEntity<Void> create(CreateProductDto productDto);

    @Operation(
            summary = "Buscar todos os produtos",
            description = "Retorna uma lista de todos os produtos cadastrados.",
            security = @SecurityRequirement(name = "Bearer")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "sucesso."),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos.", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Não autorizado.", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.", content = @Content())
    })
    ResponseEntity<List<Product>> list(
            Long id,
            String name,
            BigDecimal priceStart,
            BigDecimal priceEnd,
            BigDecimal costStart,
            BigDecimal costEnd,
            String category,
            String unit,
            String brand,
            LocalDateTime startUpdateDate,
            LocalDateTime endUpdateDate,
            LocalDateTime startRegisterDate,
            LocalDateTime endRegisterDate,
            Boolean active,
            String line,
            String ncm
    );

    @Operation(
            summary = "Atualizar informações de um produto",
            description = "Atualiza as informações de um produto existente com base no ID fornecido.",
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
            Long id, UpdateProductDto productDto
    );

    @Operation(
            summary = "Deletar um produto",
            description = "Deleta um produto do sistema com base no ID fornecido.",
            security = @SecurityRequirement(name = "Bearer")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "sucesso.", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos.", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Não autorizado.", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.", content = @Content())
    })
    ResponseEntity<Void> delete(Long id);

}
