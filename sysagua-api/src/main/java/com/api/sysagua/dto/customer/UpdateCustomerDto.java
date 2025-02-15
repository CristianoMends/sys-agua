package com.api.sysagua.dto.customer;

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

    @Schema(description = "Se o cliente esta ativo", example = "true")
    private Boolean active;

    @Valid
    private UpdateAddressDto address;

}
