package com.api.sysagua.dto.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SearchCustomerDto {
    @Schema(description = "Id do cliente.", example = "2")
    private Long id;

    @Schema(description = "Nome completo do Cliente.", example = "John Doe", maxLength = 100)
    @Size(max = 100, message = "The name must be at most 100 characters.")
    private String name;

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

    @Schema(
            description = "Número de telefone do cliente, incluindo numero do país.",
            example = "5588999999999",
            pattern = "^[0-9]{13}$"    )
    private String phone;

    @Schema(description = "Se o cliente esta ativo", example = "true")
    private Boolean active;
}
