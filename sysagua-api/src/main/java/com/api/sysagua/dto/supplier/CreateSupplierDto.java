package com.api.sysagua.dto.supplier;

import com.api.sysagua.dto.address.AddressDto;
import com.api.sysagua.model.Supplier;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Schema(description = "DTO com dados para registrar fornecedor")
public class CreateSupplierDto {

    @NotBlank(message = "Social reason cannot be blank")
    @Size(max = 100, message = "Social reason must not exceed 100 characters")
    @Schema(description = "Razão social do fornecedor", example = "Fornecedor X")
    private String socialReason;

    @NotBlank(message = "The CNPJ cannot be blank.")
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

    @Schema(description = "Nome fantasia", example = "Alpha Ltda")
    private String tradeName;

    @Schema(description = "Inscrição estadual", example = "123456789")
    @Pattern(regexp = "\\d+", message = "The state registration must contain only numbers")
    private String stateRegistration;

    @Schema(description = "Inscrição municipal", example = "987654321")
    @Pattern(regexp = "\\d+", message = "The municipal registration must contain only numbers")
    private String municipalRegistration;

    @NotBlank(message = "The phone cannot be blank.")
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

    @NotNull(message = "Address cannot be null.")
    @Valid
    @Schema(description = "Endereço do fornecedor")
    private AddressDto address;

    public Supplier toModel() {
        return new Supplier(
                getSocialReason(),
                getCnpj(),
                getAddress(),
                getPhone(),
                getTradeName(),
                getStateRegistration(),
                getMunicipalRegistration()
        );
    }
}
