package com.example.generalServiceservice.exceptions;

public class UniqueConstraintViolationException extends RuntimeException {
    public UniqueConstraintViolationException() {
        super("A record with the same value already exists. Duplicates are not allowed.");
    }
}

