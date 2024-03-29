package com.example.common.exceptions;

import com.example.common.WebClientErrorResponse;
import lombok.Getter;

@Getter
public class WebClientCustomException extends RuntimeException {
    private final WebClientErrorResponse errorResponse;

    public WebClientCustomException(WebClientErrorResponse errorResponse) {
        super(errorResponse.getMessage());
        this.errorResponse = errorResponse;
    }

}