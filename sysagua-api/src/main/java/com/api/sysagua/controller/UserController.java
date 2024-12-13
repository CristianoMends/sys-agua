package com.api.sysagua.controller;


import com.api.sysagua.dto.CreateUserDto;
import com.api.sysagua.dto.LoginDto;
import com.api.sysagua.dto.Token;
import com.api.sysagua.exception.ResponseError;
import com.api.sysagua.model.User;
import com.api.sysagua.service.TokenService;
import com.api.sysagua.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "User Controller", description = "Contém endpoints para gerenciamento de usuários")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    AuthenticationManager authenticationManager;

    @CrossOrigin
    @PostMapping
    @Operation(
            summary = "Criar um novo usuário",
            description = "Cria um novo usuário no sistema com as informações fornecidas.",
            responses = {

                    @ApiResponse(
                            responseCode = "201",
                            description = "Usuário criado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Token.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados do usuário fornecidos são inválidos",
                            content = {}
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Dados de Usuário já existe"
                    )
            }
    )

    public ResponseEntity<Void> createUser(@RequestBody @Valid CreateUserDto userDto) {
        userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("login")
    @CrossOrigin
    @Operation(
            summary = "Fazer login",
            description = "Faz login com as credenciais fornecidas.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Login realizado com sucesso",
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
                            description = "Usuário não encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseError.class)
                            )
                    )
            }
    )
    public ResponseEntity<Token> login(
            @RequestBody @Valid LoginDto loginDto
    ) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = this.tokenService.generateToken( (User) auth.getPrincipal() );
        return ResponseEntity.status(HttpStatus.OK).body(new Token(token));
    }

}
