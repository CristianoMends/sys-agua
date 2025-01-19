package com.api.sysagua.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "DTO com dados para visualizar um produto no sistema")
public class ViewProductDto {

    @Schema(description = "Identificador único do produto", example = "1")
    private Long id;

    @Schema(description = "Nome do produto", example = "Água Mineral", maxLength = 50)
    private String name;

    @Schema(description = "Unidade de medida do produto", example = "litro", maxLength = 20)
    private String unit;

    @Schema(description = "Marca do produto", example = "Crystal", maxLength = 30)
    private String brand;

    @Schema(description = "Categoria do produto", example = "Bebidas", maxLength = 30)
    private String category;

    @Schema(description = "Data de registro do produto", example = "2024-12-19")
    private LocalDate createdAt;

    @Schema(description = "Data de atualização do produto", example = "2024-12-29")
    private LocalDate updatedAt;

    @Schema(description = "Se o produto não está ativo, então foi deletado", example = "true")
    private Boolean active;
}
