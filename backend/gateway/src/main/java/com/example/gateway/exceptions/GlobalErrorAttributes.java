package com.example.gateway.exceptions;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {

        Map<String, Object> errorResponse = super.getErrorAttributes(request, options);

        HttpStatus status = HttpStatus.valueOf((Integer) errorResponse.get("status"));

        Throwable error = getError(request);

        if (error instanceof ResponseStatusException) {
            ResponseStatusException responseStatusException = (ResponseStatusException) error;
            errorResponse.put("message", responseStatusException.getReason());
        } else{
            errorResponse.put("message", "Something went wrong! Error message: " + error.getMessage());
        }

        return errorResponse;
    }
}