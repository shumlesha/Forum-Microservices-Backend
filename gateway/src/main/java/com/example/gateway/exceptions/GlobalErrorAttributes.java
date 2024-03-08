package com.example.gateway.exceptions;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {

        Map<String, Object> errorResponse = super.getErrorAttributes(request, options);
        Throwable error = super.getError(request);
        if (error instanceof org.springframework.security.access.AccessDeniedException) {
            errorResponse.put("status", HttpStatus.FORBIDDEN.value());
            errorResponse.put("message", "Access denied, maybe you have problem with your token");
            errorResponse.remove("path");
            errorResponse.remove("error");
            errorResponse.remove("requestId");
        }
        if (error instanceof com.example.common.exceptions.AccessDeniedException) {
            errorResponse.put("status", HttpStatus.FORBIDDEN.value());
            errorResponse.put("message", "Incorrect token, please check that your token is valid");
            errorResponse.remove("path");
            errorResponse.remove("error");
            errorResponse.remove("requestId");
        }

        return errorResponse;
    }
}

