package com.api.sysagua.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ResponseError {
    private LocalDateTime timestamp = LocalDateTime.now();
    private String status = "error";
    private int statusCode = 400;
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
