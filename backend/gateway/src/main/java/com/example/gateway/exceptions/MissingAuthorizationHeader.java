package com.example.gateway.exceptions;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class MissingAuthorizationHeader extends ResponseStatusException {
    public MissingAuthorizationHeader(HttpStatusCode status, String reason) {
        super(status, reason);
    }
}
