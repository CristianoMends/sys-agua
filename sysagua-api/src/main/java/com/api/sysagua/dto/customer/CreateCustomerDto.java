package com.api.sysagua.dto.customer;

import com.api.sysagua.model.Customer;
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
@Schema(description = "DTO para criação de um novo Cliente.")
public class CreateCustomerDto {

    @Schema(description = "Nome completo do Cliente.", example = "John Doe", maxLength = 100)
    @NotBlank(message = "The name cannot be blank.")
    @Size(max = 100, message = "The name must be at most 100 characters.")
    private String name;

    @Schema(description = "Endereço do cliente.")
    @NotNull(message = "The address cannot be null.")
    @Valid
    private AddressDto address;

    @Schema(
            description = "Número de telefone do cliente, incluindo numero do país.",
            example = "5588999999999",
            pattern = "^[0-9]{13}$"    )
    @NotBlank(message = "The phone cannot be blank.")
    @Pattern(
            regexp = "^[0-9]{13}$",
            message = "Phone must follow the format 5588999999999 (13 digits)."
    )
    private String phone;

    public Customer toModel() {
        return new Customer(getName(), getAddress(), getPhone());
    }
}
