package com.api.sysagua.dto.product;

import com.api.sysagua.model.ProductCategory;
import com.api.sysagua.model.ProductLine;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "DTO com dados para visualizar um produto no sistema")
public class ViewProductDto {

    @Schema(description = "Identificador único do produto", example = "1")
    private Long id;

    @Schema(description = "Nome do produto", example = "Água Mineral", maxLength = 50)
    private String name;

    @Schema(description = "Preço de venda do produto", example = "50.00")
    private Double price;

    @Schema(description = "Preço de custo do produto", example = "20.00")
    private Double cost;

    @Schema(description = "Unidade de medida do produto", example = "litro", maxLength = 20)
    private String unit;

    @Schema(description = "Marca do produto", example = "Crystal", maxLength = 30)
    private String brand;

    @Schema(description = "Categoria do produto", example = "Bebidas", maxLength = 30)
    private ProductCategory category;

    @Schema(description = "Data de registro do produto", example = "2024-12-19")
    private LocalDateTime createdAt;

    @Schema(description = "Data de atualização do produto", example = "2024-12-29")
    private LocalDateTime updatedAt;

    @Schema(description = "Se o produto não está ativo, então foi deletado", example = "true")
    private Boolean active;

    @Schema(description = "Linha do produto")
    private ProductLine line;

    @Schema(description = "A Nomenclatura Comum do Mercosul (NCM)", example = "2201.10.00")
    private String ncm;
}