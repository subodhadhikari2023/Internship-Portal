package com.subodh.InternshipPortal.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * The type Global exception handler.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {



    @ExceptionHandler(InternshipCreationFailedException.class)
    public ResponseEntity<?> handleInternshipCreationFailedException(InternshipCreationFailedException ex) {
        ExceptionResponse errorResponse = new ExceptionResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApplicationCreationFailedException.class)
    public ResponseEntity<?> handleApplicationCreationFailedException(ApplicationCreationFailedException ex) {
        ExceptionResponse errorResponse = new ExceptionResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

}
