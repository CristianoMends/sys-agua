package com.api.sysagua.docs;

import com.api.sysagua.dto.user.*;
import com.api.sysagua.enumeration.UserAccess;
import com.api.sysagua.enumeration.UserStatus;
import com.api.sysagua.exception.ResponseError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "User Controller", description = "Endpoints para gerenciamento de usuários")
public interface UserDoc {

    @Operation(
            summary = "Registrar um novo usuário",
            description = "Registra um novo usuário no sistema com as informações fornecidas.",
            security = @SecurityRequirement(name = "Bearer")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuário registrado com sucesso",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados do usuário fornecidos são inválidos",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Usuário com e-mail ou telefone já registrado",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro inesperado",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Não autorizado",
                    content = @Content()
            )
    })
    ResponseEntity<Void> create(@RequestBody @Valid CreateUserDto userDto);

    @Operation(
            summary = "Autenticar usuário",
            description = "Realiza autenticação de um usuário com as credenciais fornecidas."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Autenticação realizada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Token.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados de login inválidos",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciais incorretas ou conta inativa",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro inesperado",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "403",description = "Não autorizado", content = @Content()
            )
    })
    ResponseEntity<Token> authenticate(@RequestBody @Valid LoginDto loginDto);

    @Operation(
            summary = "Desativar usuário",
            description = "Desativa um usuário no sistema com base no e-mail fornecido.",
            security = @SecurityRequirement(name = "Bearer")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Usuário desativado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados fornecidos são inválidos",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro inesperado",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "403",description = "Não autorizado", content = @Content()
            )
    })
    ResponseEntity<Void> delete(
            @Parameter(description = "E-mail do usuário a ser desativado", required = true)
            @RequestParam @Email(message = "Email deve ser válido") String email
    );

    @Operation(
            summary = "Buscar todos os usuários",
            description = "Retorna uma lista de todos os usuários cadastrados com a possibilidade de aplicar filtros (ativos, inativos, etc.).",
            security = @SecurityRequirement(name = "Bearer")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuários encontrados com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ViewUserDto.class)
                    )),
            @ApiResponse(responseCode = "400", description = "Parâmetros de filtro inválidos",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "403",description = "Não autorizado", content = @Content()
            ),
            @ApiResponse(
                    responseCode = "500",description = "Erro inesperado", content = @Content()
            )
    })
    ResponseEntity<List<ViewUserDto>> list(
            @RequestParam(required = false) UUID id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) UserStatus status,
            @RequestParam(required = false) UserAccess access
    );

    @Operation(
            summary = "Atualizar informações de um usuário",
            description = "Atualiza as informações de um usuário existente com base no ID fornecido.",
            security = @SecurityRequirement(name = "Bearer")

    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Usuário atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ViewUserDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados fornecidos são inválidos",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "403",description = "Não autorizado", content = @Content()
            ),
            @ApiResponse(
                    responseCode = "500",description = "Erro inesperado", content = @Content()
            )
    })
    public ResponseEntity<Void> update(
            @RequestParam UUID id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) UserStatus status,
            @RequestParam(required = false) UserAccess access
    );

}
