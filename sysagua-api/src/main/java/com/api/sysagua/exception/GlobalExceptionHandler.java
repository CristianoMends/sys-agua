package com.api.sysagua.exception;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.*;

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

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ResponseError> handleAccessDeniedException(AccessDeniedException e, WebRequest request) {
        return buildResponse("You do not have permission to access this resource", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseError> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errorMessages = new ArrayList<>();
        int i=1;
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {

            String fieldName = fieldError.getField();

            String errorMessage = String.format("%d - The '%s' data provided in the '%s' field is invalid. Please check and try again.", i++,fieldError.getRejectedValue(), fieldName);
            errorMessages.add(errorMessage);
        }

        String message = String.join(", ", errorMessages);

        return buildResponse(message, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({BadCredentialsException.class, InternalAuthenticationServiceException.class})
    protected ResponseEntity<ResponseError> handleAuthenticationExceptions(RuntimeException e, WebRequest request) {
        return buildResponse("Nonexistent username or invalid password", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ResponseError> handleBusinessException(BusinessException e, WebRequest request) {
        return buildResponse(e.getMessage(), e.getStatus());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ResponseError> handleGeneralException(Exception e, WebRequest request) {
        logger.error("Unhandled exception: ", e);
        return buildResponse("An unexpected error has occurred. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
