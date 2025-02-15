package com.api.sysagua.dto.address;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAddressDto {

    @Schema(
            description = "Número de residencia do fornecedor",
            example = "20"
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
