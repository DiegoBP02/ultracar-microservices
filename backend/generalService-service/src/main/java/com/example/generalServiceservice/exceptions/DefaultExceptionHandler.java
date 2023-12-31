package com.example.generalServiceservice.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class DefaultExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> Exception(Exception e, HttpServletRequest request) {
        logger.error("Exception occurred:", e);
        String error = "Server error";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        StandardError err = new StandardError(Instant.now(), status.value(), error,
                e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(UniqueConstraintViolationException.class)
    public ResponseEntity<StandardError> UniqueConstraintViolationException
            (UniqueConstraintViolationException e, HttpServletRequest request) {
        String error = "Duplicate entry found. Please provide a unique value";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(Instant.now(), status.value(), error,
                e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandardError> IllegalArgumentException
            (IllegalArgumentException e, HttpServletRequest request) {
        String error = "Invalid input provided";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(Instant.now(), status.value(), error,
                e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> ResourceNotFoundException
            (ResourceNotFoundException e, HttpServletRequest request) {
        logger.error("Resource not found exception:", e);
        String error = "Resource not found exception";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(Instant.now(), status.value(), error,
                e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }
}
