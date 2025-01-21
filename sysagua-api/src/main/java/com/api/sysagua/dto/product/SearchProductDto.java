package com.api.sysagua.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @Schema(description = "Preço de venda inicial", example = "5.00")
    private Double priceStart;

    @Schema(description = "Preço de venda final", example = "50.00")
    private Double priceEnd;

    @Schema(description = "Preço de custo inicial", example = "1.00")
    private Double costStart;

    @Schema(description = "Preço de custo final", example = "100.00")
    private Double costEnd;

    @Schema(description = "Unidade de medida do produto", example = "litro", maxLength = 20)
    @Size(max = 20, message = "Unit should not exceed 20 characters")
    private String unit;

    @Schema(description = "Marca do produto", example = "Crystal", maxLength = 30)
    @Size(max = 30, message = "Brand should not exceed 30 characters")
    private String brand;

    @Schema(description = "Categoria do produto", example = "Bebidas", maxLength = 30)
    @Size(max = 30, message = "Category should not exceed 30 characters")
    private String category;

    @Schema(description = "Data inicial do registro para busca", example = "2025-01-20T21:27:18.645684")
    @PastOrPresent(message = "Start register date should not be in the future")
    private LocalDateTime startRegisterDate;

    @Schema(description = "Data final do registro para busca", example = "2025-01-20T21:27:18.645684")
    @FutureOrPresent(message = "End register date should not be in the past")
    private LocalDateTime endRegisterDate;

    @Schema(description = "Data inicial da atualização para busca", example = "2025-01-20T21:27:18.645684")
    @PastOrPresent(message = "Start update date should not be in the future")
    private LocalDateTime startUpdateDate;

    @Schema(description = "Data final da atualização para busca", example = "2025-01-20T21:27:18.645684")
    private LocalDateTime endUpdateDate;

    @Schema(description = "Se o produto não está ativo, então foi deletado", example = "true")
    private Boolean active;

    @Schema(description = "A Nomenclatura Comum do Mercosul (NCM)", example = "2201.10.00")
    private String ncm;

    @Schema(description = "Uma linha de produtos é uma categoria que lhe permite segmentar os seus produtos de acordo com determinadas especificações", example = "1")
    private String line;
}
