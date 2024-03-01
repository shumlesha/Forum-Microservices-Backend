package com.example.common;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.util.Map;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private HttpStatusCode statusCode;
    private String message;
    private Map<String, String> errors;

    public ErrorResponse(HttpStatusCode status, String message) {
        this.statusCode = status;
        this.message = message;
    }
}
