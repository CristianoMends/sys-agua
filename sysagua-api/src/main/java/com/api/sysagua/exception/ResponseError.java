package com.api.sysagua.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Representa um erro na resposta da API")
public class ResponseError {

    @Schema(description = "Data e hora em que o erro ocorreu", example = "2024-12-12T14:30:00")
    private LocalDateTime timestamp = LocalDateTime.now();

    @Schema(description = "Status da resposta", example = "error")
    private String status = "error";

    @Schema(description = "Código de status HTTP do erro", example = "400")
    private int statusCode = 400;

    @Schema(description = "Descrição do erro", example = "Usuário ou senha incorretos")
    private String error;

    public ResponseError(String error, int statusCode) {
        this.error = error;
        this.statusCode = statusCode;
        this.timestamp = LocalDateTime.now();
    }

    public ResponseError(String error, int statusCode, String status) {
        this.error = error;
        this.statusCode = statusCode;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }
}
