package com.api.sysagua.controller;


import com.api.sysagua.dto.LoginDto;
import com.api.sysagua.dto.Token;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/users")
@Tag(name = "User Controller", description = "Contém endpoints para gerenciamento de usuários")
public class UserController {


    @PostMapping("login")
    @CrossOrigin
    @Operation(summary = "Fazer login", description = "Faz login com as credenciais fornecidas.")
    public ResponseEntity<Token> login(
            @RequestBody LoginDto loginDto
    ) {
        if (!loginDto.email().equals("usuario@gmail.com") || !loginDto.password().equals("qwe123")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(new Token("qwe121qw2e1q2w1e2q1sd"));
    }


}
