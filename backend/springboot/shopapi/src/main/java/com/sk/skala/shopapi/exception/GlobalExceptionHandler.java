package com.sk.skala.shopapi.exception;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sk.skala.shopapi.common.Response;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseException.class)
    public ResponseEntity<Response<Void>> handleResponseException(ResponseException e) {
        Error error = e.getError();
        return ResponseEntity.status(error.getStatus())
                .body(Response.of(error.getStatus().value(), e.getMessage(), null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<Void>> handleValidation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Response.of(HttpStatus.BAD_REQUEST.value(), message, null));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<Response<Void>> handleMissingHeader(MissingRequestHeaderException e) {
        Error error = Error.UNAUTHORIZED;
        return ResponseEntity.status(error.getStatus())
                .body(Response.of(error.getStatus().value(), error.getMessage(), null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Void>> handleException(Exception e) {
        log.error("Unexpected error", e);
        Error error = Error.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(error.getStatus())
                .body(Response.of(error.getStatus().value(), error.getMessage(), null));
    }
}
