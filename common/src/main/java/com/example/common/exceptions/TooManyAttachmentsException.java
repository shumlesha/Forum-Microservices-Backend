package com.example.common.exceptions;

public class TooManyAttachmentsException extends RuntimeException {
    public TooManyAttachmentsException(String message) {
        super(message);
    }
}
