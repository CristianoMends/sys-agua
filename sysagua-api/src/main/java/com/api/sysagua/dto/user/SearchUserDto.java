package com.api.sysagua.dto.user;

import com.api.sysagua.enumeration.UserAccess;
import com.api.sysagua.enumeration.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "DTO com dados para buscar de usuário no sistema")
public class SearchUserDto {

    @Schema(description = "Identificador único do usuário", example = "e7b8c20c-22d8-4327-b9fa-028dfbdbf2f9")
    private UUID id = null;

    @Schema(description = "Nome do usuário", example = "João", maxLength = 20)
    @Size(max = 20, message = "Name should not exceed 20 characters")
    private String name = "";

    @Schema(description = "Sobrenome do usuário", example = "Silva", maxLength = 30)
    @Size(max = 30, message = "Surname should not exceed 30 characters")
    private String surname = "";

    @Schema(description = "Número de telefone do usuário", example = "1234567890", pattern = "\\d{10,11}", nullable = true)
    @Pattern(regexp = "\\d{10,11}", message = "Phone must contain 10 or 11 digits")
    private String phone = "";

    @Schema(description = "Endereço de e-mail do usuário", example = "joao.silva@gmail.com")
    @Email(message = "Email should be valid")
    @Pattern(
            regexp = "^[A-Za-z]+[.A-Za-z0-9]+[A-Za-z0-9]+@(gmail\\.com|outlook\\.com|hotmail\\.com|yahoo\\.com)$",
            message = "Email should be valid")
    private String email = "";

    @Schema(description = "Status do usuário", example = "ACTIVE")
    @Pattern(regexp = "ACTIVE|INACTIVE", message = "Status must be one of: ACTIVE, INACTIVE")
    private UserStatus status = null;

    @Schema(description = "Tipo de acesso do usuário", example = "OWNER")
    @Pattern(regexp = "OWNER|EMPLOYEE|DEVELOPER|MANAGER", message = "Access must be one of: OWNER, EMPLOYEE, DEVELOPER, MANAGER")
    private UserAccess access = null;
}
