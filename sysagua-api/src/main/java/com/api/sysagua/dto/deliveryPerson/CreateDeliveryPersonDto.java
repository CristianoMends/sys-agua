package com.api.sysagua.dto.deliveryPerson;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "DTO para criação de um novo Entregador.")
public class CreateDeliveryPersonDto {

    @Schema(description = "Nome do entregador", example = "Paulo", maxLength = 20)
    @NotBlank(message = "Name is mandatory")
    @Size(max = 20, message = "Name should not exceed 20 characters")
    private String name;

    @Schema(description = "Número do entregador", example = "0521364789", pattern = "\\d{10,11}", nullable = true)
    @Pattern(regexp = "\\d{10,11}", message = "Phone must contain 10 or 11 digits")
    private String phone;

}
