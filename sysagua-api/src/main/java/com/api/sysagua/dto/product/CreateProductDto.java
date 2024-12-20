package com.api.sysagua.dto.product;

import com.api.sysagua.model.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "DTO com dados para criar um novo produto no sistema")
public class CreateProductDto {

    @Schema(description = "Nome do produto", example = "Água Mineral", maxLength = 50)
    @NotBlank(message = "Name is mandatory")
    @Size(max = 50, message = "Name should not exceed 50 characters")
    private String name;

    @Schema(description = "Unidade de medida do produto", example = "UNIDADE", maxLength = 20)
    @NotBlank(message = "Unit is mandatory")
    @Size(max = 20, message = "Unit should not exceed 20 characters")
    private String unit;

    @Schema(description = "Preço por unidade", example = "2.50")
    @NotNull(message = "Price per unit is mandatory")
    private Double price;

    @Schema(description = "Custo do produto", example = "1.50")
    @NotNull(message = "Cost is mandatory")
    private Double cost;

    @Schema(description = "Marca do produto", example = "Crystal", maxLength = 30)
    @Size(max = 30, message = "Brand should not exceed 30 characters")
    private String brand;

    @Schema(description = "Categoria do produto", example = "Bebidas", maxLength = 30)
    @Size(max = 30, message = "Category should not exceed 30 characters")
    private String category;

    public Product toModel() {
        return new Product(
                name,
                unit,
                price,
                cost,
                brand,
                category
        );
    }
}
