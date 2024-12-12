package com.api.sysagua.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO que representa um token de autenticação JWT.
 */
@Schema(description = "DTO que contém o token de autenticação JWT gerado para o usuário")
public record Token(

        @Schema(description = "O token JWT gerado após o login", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoxMjM0NTY3ODkwLCJpYXQiOjE2MTc5Mzg3NzksImV4cCI6MTYxODAxMzc3OX0.VyA5FtkErLzLqkzqMhcbqNnJEXhGcjybY0aG_gTwAFA")
        String token) {}
