package com.api.sysagua.dto.supplier;

import com.api.sysagua.dto.customer.AddressDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
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
public class UpdateSupplierDto {

    @Size(max = 100, message = "Social reason must not exceed 100 characters")
    @Schema(description = "Razão social do fornecedor", example = "Fornecedor X")
    private String socialReason;

    @Pattern(
            regexp = "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}",
            message = "The CNPJ must follow the format 00.000.000/0000-00."
    )
    @Schema(
            description = "Número do CNPJ do fornecedor",
            example = "04.693.497/0001-21",
            pattern = "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}"
    )
    private String cnpj;

    @Pattern(
            regexp = "^[0-9]{13}$",
            message = "Phone must follow the format 5588999999999 (13 digits)."
    )
    @Schema(
            description = "Número de telefone do fornecedor, incluindo código do país",
            example = "5588999999999",
            pattern = "^[0-9]{13}$"
    )
    private String phone;

    @Pattern(
            regexp = "^\\d+(?: [a-zA-Z]+(?: [a-zA-Z]+)*)?$",
            message = "The number format must be '204 B' or '204 B High', where the text after the number is optional."
    )
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

    @Schema(description = "Nome do estado.", example = "Ceará", maxLength = 20)
    @Size(max = 20, message = "The state must be at most 20 characters.")
    private String state;

}
