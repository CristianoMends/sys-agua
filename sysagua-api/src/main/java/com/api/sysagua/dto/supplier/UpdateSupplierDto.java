package com.api.sysagua.dto.supplier;

import com.api.sysagua.dto.address.UpdateAddressDto;
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

    @Schema(description = "Nome fantasia")
    private String tradeName;

    @Schema(description = "Inscrição estadual")
    private String stateRegistration;

    @Schema(description = "Inscrição municipal")
    private String municipalRegistration;

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

    @Valid
    private UpdateAddressDto address;

}
