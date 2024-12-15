package com.api.sysagua.controller;

import com.api.sysagua.dto.*;
import com.api.sysagua.enumeration.UserAccess;
import com.api.sysagua.enumeration.UserStatus;
import com.api.sysagua.exception.ResponseError;
import com.api.sysagua.model.User;
import com.api.sysagua.service.UserService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "User Controller", description = "Endpoints para gerenciamento de usuários")
public class UserController {

    @Autowired
    private UserService userService;

    @CrossOrigin
    @PostMapping
    @Operation(
            summary = "Registrar um novo usuário",
            description = "Registra um novo usuário no sistema com as informações fornecidas.",
            security = @SecurityRequirement(name = "Bearer")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuário registrado com sucesso",
                    content = @Content(
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados do usuário fornecidos são inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Usuário com e-mail ou telefone já registrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseError.class)
                    )
            )
    })
    public ResponseEntity<Void> registerUser(@RequestBody @Valid CreateUserDto userDto) {
        userService.registerUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @CrossOrigin
    @PostMapping("/login")
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
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciais incorretas ou conta inativa",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseError.class)
                    )
            )
    })
    public ResponseEntity<Token> authenticateUser(@RequestBody @Valid LoginDto loginDto) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.authenticateUser(loginDto));
    }

    @DeleteMapping()
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
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseError.class)
                    )
            )
    })
    public ResponseEntity<Void> deactivateUser(
            @Parameter(description = "E-mail do usuário a ser desativado", required = true)
            @RequestParam @Email(message = "Email deve ser válido") String email
    ) {
        this.userService.deactivateUser(email);
        return ResponseEntity.noContent().build();
    }


    @GetMapping
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
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseError.class)
                    ))
    })
    @CrossOrigin
    public ResponseEntity<List<ViewUserDto>> getUsers(
            @RequestParam(required = false) UUID id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) UserStatus status,
            @RequestParam(required = false) UserAccess access
    ) {
        var searchUserDto = new SearchUserDto(id, name, surname, phone, email, status, access);
        List<ViewUserDto> users = userService.getUsers(searchUserDto)
                .stream()
                .map(User::toView)
                .toList();
        return ResponseEntity.ok().body(users);
    }

    @PutMapping()
    @Operation(
            summary = "Atualizar informações de um usuário",
            description = "Atualiza as informações de um usuário existente com base no ID fornecido.",
            security = @SecurityRequirement(name = "Bearer")

    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ViewUserDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados fornecidos são inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseError.class)
                    )
            )
    })
    @CrossOrigin
    public ResponseEntity<Void> updateUser(
            @RequestParam UUID id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) UserStatus status,
            @RequestParam(required = false) UserAccess access
    ) {
        var userDto = new UpdateUserDto(id, name, surname, phone, email, status, access);
        userService.updateUser(userDto);
        return ResponseEntity.noContent().build();
    }


}
