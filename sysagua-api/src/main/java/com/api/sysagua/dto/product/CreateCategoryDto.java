package com.api.sysagua.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "DTO com dados para criar uma nova categoria de produto no sistema")
public class CreateCategoryDto {

    @Schema(description = "Nome da categoria", example = "Água Mineral", maxLength = 50)
    String name;
}
