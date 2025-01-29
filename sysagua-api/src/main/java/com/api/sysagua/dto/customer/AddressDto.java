package com.api.sysagua.dto.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO representando o endereço de um cliente.")
public class AddressDto {

    @Schema(description = "Numero de residencia do cliente.", example = "123", maxLength = 10)
    @NotBlank(message = "The number cannot be blank.")
    @Size(max = 10, message = "The number must be at most 10 characters.")

    @Pattern(
            regexp = "^\\d+(?: [a-zA-Z]+(?: [a-zA-Z]+)*)?$",
            message = "The number format must be '204 B' or '204 B High', where the text after the number is optional."
    )
    private String number;

    @Schema(description = "Nome da rua.", example = "Rua torres tortas", maxLength = 50)
    @NotBlank(message = "The street cannot be blank.")
    @Size(max = 50, message = "The street must be at most 50 characters.")
    private String street;

    @Schema(description = "Nome do Bairro.", example = "Centro", maxLength = 50)
    @NotBlank(message = "The neighborhood cannot be blank.")
    @Size(max = 50, message = "The neighborhood must be at most 50 characters.")
    private String neighborhood;

    @Schema(description = "Nome da cidade.", example = "Springfield", maxLength = 50)
    @NotBlank(message = "The city cannot be blank.")
    @Size(max = 50, message = "The city must be at most 50 characters.")
    private String city;

    @Schema(description = "Nome do estado.", example = "CE", maxLength = 20)
    @NotBlank(message = "The state cannot be blank.")
    @Size(max = 20, message = "The state must be at most 20 characters.")
    private String state;
}
