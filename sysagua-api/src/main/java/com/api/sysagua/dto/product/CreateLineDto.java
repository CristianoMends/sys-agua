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
@Schema(description = "DTO com dados para criar um nova Linha de produto no sistema")
public class CreateLineDto {

    @Schema(description = "Nome da linha", example = "Descartavel", maxLength = 50)
    String name;
}
