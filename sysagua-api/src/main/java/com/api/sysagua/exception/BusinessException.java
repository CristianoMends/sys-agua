package com.api.sysagua.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {
    private HttpStatus status = HttpStatus.BAD_REQUEST;

    public BusinessException(String message) {
        super(message);
    }
    public BusinessException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
    public BusinessException(String message, Object ... params) {
        super(String.format(message, params));
    }

}