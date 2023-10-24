package com.example.gateway.exceptions;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class InvalidTokenException extends ResponseStatusException {
    public InvalidTokenException(HttpStatusCode status, String reason) {
        super(status, reason);
    }
}
