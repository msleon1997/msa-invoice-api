package com.invoice.api.controller;

import com.invoice.api.exceptions.NotContentException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(NotContentException.class)
    ResponseEntity<Void> handleNotContentException(NotContentException ex) {
        return ResponseEntity.noContent().build();
    }

}

