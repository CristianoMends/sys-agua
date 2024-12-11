package com.api.sysagua.controller;


import com.api.sysagua.dto.CreateUserDto;
import com.api.sysagua.dto.LoginDto;
import com.api.sysagua.dto.Token;
import com.api.sysagua.model.User;
import com.api.sysagua.service.AuthorizationService;
import com.api.sysagua.service.TokenService;
import com.api.sysagua.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
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
@Tag(name = "User Controller", description = "Contém endpoints para gerenciamento de usuários")
public class UserController {

    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    AuthenticationManager authenticationManager;

    @CrossOrigin
    @PostMapping("create")
    public ResponseEntity<String> createUser(@RequestBody @Valid CreateUserDto userDto) {
        userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }

    @PostMapping("login")
    @CrossOrigin
    @Operation(summary = "Fazer login", description = "Faz login com as credenciais fornecidas.")
    public ResponseEntity<Token> login(
            @RequestBody LoginDto loginDto
    ) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = this.tokenService.generateToken( (User) auth.getPrincipal() );
        return ResponseEntity.status(HttpStatus.OK).body(new Token(token));
    }

}
