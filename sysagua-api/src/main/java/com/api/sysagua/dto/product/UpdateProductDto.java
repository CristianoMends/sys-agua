package com.api.sysagua.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "DTO com dados para atualizar um produto no sistema")
public class UpdateProductDto {

    @Schema(description = "Nome do produto", example = "Água Mineral", maxLength = 50)
    @Size(max = 50, message = "Name should not exceed 50 characters")
    private String name;

    @Schema(description = "Unidade de medida do produto", example = "litro", maxLength = 20)
    @Size(max = 20, message = "Unit should not exceed 20 characters")
    private String unit;

    @Schema(description = "Marca do produto", example = "Crystal", maxLength = 30)
    @Size(max = 30, message = "Brand should not exceed 30 characters")
    private String brand;

    @Schema(description = "Categoria do produto", example = "Bebidas", maxLength = 30)
    @Size(max = 30, message = "Category should not exceed 30 characters")
    private String category;

    @Schema(description = "Se o produto não está ativo, então foi deletado", example = "true")
    private Boolean active;
}
