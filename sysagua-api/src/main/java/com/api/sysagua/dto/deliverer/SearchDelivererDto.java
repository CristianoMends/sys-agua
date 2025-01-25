package com.api.sysagua.dto.deliverer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SearchDelivererDto {
    @Schema(description = "Id do entregador.", example = "47")
    private Long id;

    @Schema(description = "Nome do entregador.", example = "Paulo", maxLength = 100)
    @Size(max = 100, message = "The name must be at most 100 characters.")
    private String name;

    @Schema(description = "Número de telefone do entregador", example = "0521364789", pattern = "\\d{10,11}", nullable = true)
    @Pattern(regexp = "\\d{10,11}", message = "Phone must contain 10 or 11 digits")
    private String phone = "";
}
