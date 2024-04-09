package com.example.common.exceptions;

public class NotFileOwnerException extends RuntimeException {
    public NotFileOwnerException(String message) {
        super(message);
    }
}
