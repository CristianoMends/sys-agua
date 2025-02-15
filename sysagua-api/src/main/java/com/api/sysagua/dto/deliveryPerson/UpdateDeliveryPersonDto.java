package com.api.sysagua.dto.deliveryPerson;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDeliveryPersonDto {
    @Schema(description = "Id do entregador.", example = "47")
    private Long id;

    @Schema(description = "Nome do entregador.", example = "Paulo", maxLength = 100)
    @Size(max = 100, message = "The name must be at most 100 characters.")
    private String name;

    @Schema(description = "Número de telefone do entregador", example = "0521364789", pattern = "\\d{10,11}", nullable = true)
    @Pattern(regexp = "\\d{10,11}", message = "Phone must contain 10 or 11 digits")
    private String phone = "";
}
