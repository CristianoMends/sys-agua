package com.api.sysagua.dto;

import com.api.sysagua.enumeration.Access;
import com.api.sysagua.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "DTO com dados para criar um novo usuário no sistema")
public class CreateUserDto {

    @Schema(description = "Nome do usuário", example = "João", maxLength = 20, $comment = "teste")
    @NotBlank(message = "Name is mandatory")
    @Size(max = 20, message = "Name should not exceed 20 characters")
    private String name;

    @Schema(description = "Sobrenome do usuário", example = "Silva", maxLength = 30)
    @Size(max = 30, message = "Surname should not exceed 30 characters")
    private String surname;

    @Schema(description = "Número de telefone do usuário", example = "1234567890", pattern = "\\d{10,11}")
    @NotBlank(message = "Phone is mandatory")
    @Pattern(regexp = "\\d{10,11}", message = "Phone must contain 10 or 11 digits")
    private String phone;

    @Schema(description = "Endereço de e-mail do usuário", example = "joao.silva@gmail.com")
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Pattern(
            regexp = "^[A-Za-z]+[.A-Za-z0-9]+[A-Za-z0-9]+@(gmail\\.com|outlook\\.com|hotmail\\.com|yahoo\\.com)$",
            message = "Email should be valid")
    private String email;

    @Schema(description = "Senha do usuário", example = "senha123", minLength = 6, maxLength = 20)
    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, max = 20, message = "Password should have between 6 and 20 characters")
    private String password;

    @Schema(description = "Tipo de acesso do usuário", example = "ADMIN")
    private Access access;

    public User toModel(){
        return new User(
                getName(),
                getSurname(),
                getPhone(),
                getEmail(),
                getPassword(),
                getAccess()
        );
    }

}