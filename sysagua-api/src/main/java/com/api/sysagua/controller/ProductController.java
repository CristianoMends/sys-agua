package com.api.sysagua.controller;

import com.api.sysagua.dto.product.CreateProductDto;
import com.api.sysagua.dto.product.SearchProductDto;
import com.api.sysagua.dto.product.UpdateProductDto;
import com.api.sysagua.dto.product.ViewProductDto;
import com.api.sysagua.exception.ResponseError;
import com.api.sysagua.model.Product;
import com.api.sysagua.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/products")
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Product Controller", description = "Endpoints para gerenciamento de produtos")
public class ProductController {

    @Autowired
    private ProductService productService;

    @CrossOrigin
    @PostMapping
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
            )
    })
    public ResponseEntity<Void> registerProduct(@RequestBody @Valid CreateProductDto productDto) {
        productService.registerProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
/*
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Deletar produto",
            description = "Deleta um produto do sistema com base no ID fornecido.",
            security = @SecurityRequirement(name = "Bearer")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Produto deletado com sucesso"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Produto não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseError.class)
                    )
            )
    })
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID do produto a ser deletado", required = true)
            @PathVariable UUID id
    ) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
    */

    @GetMapping
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
    @CrossOrigin
    public ResponseEntity<List<ViewProductDto>> getProducts(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String unit,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) LocalDate startUpdateDate,
            @RequestParam(required = false) LocalDate endUpdateDate,
            @RequestParam(required = false) LocalDate startRegisterDate,
            @RequestParam(required = false) LocalDate endRegisterDate,
            @RequestParam(required = false) Double minCost,
            @RequestParam(required = false) Double maxCost,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice
    ) {

        var searchProductDto = new SearchProductDto(
                id,
                name,
                unit,
                brand,
                category,
                startUpdateDate,
                endUpdateDate,
                startRegisterDate,
                endRegisterDate,
                minCost,
                maxCost,
                minPrice,
                maxPrice
        );

        List<ViewProductDto> products = productService.getProducts(searchProductDto)
                .stream()
                .map(Product::toView)
                .toList();

        return ResponseEntity.ok().body(products);
    }

    @PutMapping()
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
            )
    })
    public ResponseEntity<Void> updateProduct(
            @RequestParam Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String unit,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) Double cost
    ) {
        UpdateProductDto productDto = new UpdateProductDto(name, unit,brand, category, price, cost);
        this.productService.updateProduct(id, productDto);
        return ResponseEntity.noContent().build();
    }
}
