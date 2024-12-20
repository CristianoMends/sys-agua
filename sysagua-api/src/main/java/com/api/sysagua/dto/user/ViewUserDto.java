package com.api.sysagua.dto.user;

import com.api.sysagua.enumeration.UserAccess;
import com.api.sysagua.enumeration.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "DTO com dados para visualizar um usuário no sistema")
public class ViewUserDto {

    @Schema(description = "id do usuário", example = "16704898-2790-4b0c-878a-3b3714aa4712")
    private UUID id;

    @Schema(description = "Nome do usuário", example = "João", maxLength = 20)
    private String name;

    @Schema(description = "Sobrenome do usuário", example = "Silva", maxLength = 30)
    private String surname;

    @Schema(description = "Número de telefone do usuário", example = "1234567890", pattern = "\\d{10,11}", nullable = true)
    private String phone;

    @Schema(description = "Endereço de e-mail do usuário", example = "joao.silva@gmail.com")
    private String email;

    @Schema(description = "status do usuário", example = "ACTIVE")
    private UserStatus status;

    @Schema(description = "Tipo de acesso do usuário", example = "ADMIN")
    private UserAccess access;

}
