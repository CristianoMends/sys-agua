package com.api.sysagua.exception;

import jakarta.annotation.Resource;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Resource
    private MessageSource messageSource;

    private HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private ResponseError responseError(String message, HttpStatus statusCode) {
        ResponseError responseError = new ResponseError();
        responseError.setStatus("error");
        responseError.setError(message);
        responseError.setStatusCode(statusCode.value());
        return responseError;
    }

    // Método para tratar erros de validação
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();

        // Criando um mapa para armazenar os erros por campo
        Map<String, String> errors = new HashMap<>();

        // Iterando sobre os erros de validação e adicionando ao mapa
        for (ObjectError error : result.getAllErrors()) {
            // A chave será uma mensagem genérica (como nome do DTO ou outro campo)
            String errorMessage = error.getDefaultMessage(); // mensagem de erro
            errors.put("createUserDto", errorMessage); // Adicionando erro com chave genérica
        }

        // Criando a resposta com timestamp e status code
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", new Date().toString());
        response.put("status", "error");
        response.put("statusCode", HttpStatus.BAD_REQUEST.value());
        response.put("errors", errors);

        // Retornando o erro como um JSON estruturado
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    // Handler para exceções gerais (qualquer outro tipo de erro)
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleGeneral(Exception e, WebRequest request) {
        // Log para monitoramento
        logger.error("Unhandled exception: ", e);

        String message = "An unexpected error has occurred. Please try again later.";
        ResponseError error = responseError(message, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(error, headers(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handler para exceções de negócios específicas
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<Object> handleBusinessException(BusinessException e, WebRequest request) {
        // Log para monitoramento
        logger.warn("Business exception: ", e);

        ResponseError error = responseError(e.getMessage(), HttpStatus.CONFLICT);
        return new ResponseEntity<>(error, headers(), HttpStatus.CONFLICT);
    }
}
