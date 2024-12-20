package com.api.sysagua.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "DTO com dados para realizar login no sistema")
public class LoginDto{
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Pattern(
            regexp = "^[A-Za-z]+[.A-Za-z0-9]+[A-Za-z0-9]+@(gmail\\.com|outlook\\.com|hotmail\\.com|yahoo\\.com)$",
            message = "Email should be valid")
    @Schema(description = "Email do usuário", example = "usuario@gmail.com")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, max = 20, message = "Password should have between 6 and 20 characters")
    @Schema(description = "Senha do usuário", example = "senha123")
    private String password;
}

