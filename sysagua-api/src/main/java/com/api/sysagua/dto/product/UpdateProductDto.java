package com.api.sysagua.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "DTO com dados para atualizar um produto no sistema")
public class UpdateProductDto {

    @Schema(description = "Nome do produto", example = "Água Mineral", maxLength = 50)
    @Size(max = 50, message = "Name should not exceed 50 characters")
    private String name;

    @Schema(description = "Valor de custo do produto", example = "47.59")
    private BigDecimal cost;

    @Schema(description = "Preço de venda do produto ao ser adicionado ao estoque", example = "50.00")
    private BigDecimal price;

    @Schema(description = "Unidade de medida do produto", example = "litro", maxLength = 20)
    @Size(max = 20, message = "Unit should not exceed 20 characters")
    private String unit;

    @Schema(description = "Marca do produto", example = "Crystal", maxLength = 30)
    @Size(max = 30, message = "Brand should not exceed 30 characters")
    private String brand;

    @Schema(description = "id unico da categoria do produto", example = "1")
    private Long categoryId;

    @Schema(description = "id unico da linha do produto", example = "1")
    private Long lineId;

    @Schema(description = "A Nomenclatura Comum do Mercosul (NCM)", example = "2201.10.00")
    private String ncm;
}
