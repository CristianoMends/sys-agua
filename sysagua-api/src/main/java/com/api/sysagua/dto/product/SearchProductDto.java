package com.api.sysagua.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "DTO para busca de produtos no sistema")
public class SearchProductDto {

    @Schema(description = "Identificador único do produto", example = "1")
    private Long id;

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

    @Schema(description = "Data inicial do registro para busca", example = "2023-01-01")
    @PastOrPresent(message = "Start register date should not be in the future")
    private LocalDate startRegisterDate;

    @Schema(description = "Data final do registro para busca", example = "2023-12-31")
    @FutureOrPresent(message = "End register date should not be in the past")
    private LocalDate endRegisterDate;

    @Schema(description = "Data inicial da atualização para busca", example = "2023-01-01")
    @PastOrPresent(message = "Start update date should not be in the future")
    private LocalDate startUpdateDate;

    @Schema(description = "Data final da atualização para busca", example = "2023-12-31")
    @FutureOrPresent(message = "End update date should not be in the past")
    private LocalDate endUpdateDate;

    @Schema(description = "Se o produto não está ativo, então foi deletado", example = "true")
    private Boolean active;
}
