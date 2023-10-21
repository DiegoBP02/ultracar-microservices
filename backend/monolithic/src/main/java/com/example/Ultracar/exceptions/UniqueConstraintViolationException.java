package com.example.Ultracar.exceptions;

public class UniqueConstraintViolationException extends RuntimeException {
    public UniqueConstraintViolationException() {
        super("A record with the same value already exists. Duplicates are not allowed.");
    }

    public UniqueConstraintViolationException(String entity, String property) {
        super("A " + entity + " with the same " + property +
                " already exists. Duplicates are not allowed.");
    }
}

