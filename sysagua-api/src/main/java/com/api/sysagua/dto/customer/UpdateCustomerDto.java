package com.api.sysagua.dto.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
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
public class UpdateCustomerDto {

    @Schema(description = "Nome completo do Cliente.", example = "John Doe", maxLength = 100)
    @Size(max = 100, message = "The name must be at most 100 characters.")
    private String name;

    @Schema(
            description = "Número de telefone do cliente, incluindo numero do país.",
            example = "5588999999999",
            pattern = "^[0-9]{13}$"    )
    @Pattern(
            regexp = "^[0-9]{13}$",
            message = "Phone must follow the format 5588999999999 (13 digits)."
    )
    private String phone;

    @Schema(description = "Se o cliente esta ativo", example = "true")
    private Boolean active;

    @Schema(description = "Numero de residencia do cliente.", example = "123", maxLength = 10)
    @Size(max = 10, message = "The number must be at most 10 characters.")

    @Pattern(
            regexp = "^\\d+(?: [a-zA-Z]+(?: [a-zA-Z]+)*)?$",
            message = "The number format must be '204 B' or '204 B High', where the text after the number is optional."
    )
    private String number;

    @Schema(description = "Nome da rua.", example = "Rua torres tortas", maxLength = 50)
    @Size(max = 50, message = "The street must be at most 50 characters.")
    private String street;

    @Schema(description = "Nome do Bairro.", example = "Centro", maxLength = 50)
    @Size(max = 50, message = "The neighborhood must be at most 50 characters.")
    private String neighborhood;

    @Schema(description = "Nome da cidade.", example = "Springfield", maxLength = 50)
    @Size(max = 50, message = "The city must be at most 50 characters.")
    private String city;

    @Schema(description = "Nome do estado.", example = "CE", maxLength = 20)
    @Size(max = 20, message = "The state must be at most 20 characters.")
    private String state;

}
