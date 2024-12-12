package com.api.sysagua.exception;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
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

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private ResponseEntity<ResponseError> buildResponse(String message, HttpStatus status) {
        ResponseError error = new ResponseError();
        error.setStatus("error");
        error.setError(message);
        error.setStatusCode(status.value());
        return ResponseEntity.status(status).headers(createHeaders()).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseError> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return buildResponse(ex.getAllErrors().get(0).getDefaultMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BadCredentialsException.class, InternalAuthenticationServiceException.class})
    protected ResponseEntity<ResponseError> handleAuthenticationExceptions(RuntimeException e, WebRequest request) {
        return buildResponse("Nonexistent username or invalid password", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ResponseError> handleBusinessException(BusinessException e, WebRequest request) {
        return buildResponse(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ResponseError> handleGeneralException(Exception e, WebRequest request) {
        logger.error("Unhandled exception: ", e);
        return buildResponse("An unexpected error has occurred. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
