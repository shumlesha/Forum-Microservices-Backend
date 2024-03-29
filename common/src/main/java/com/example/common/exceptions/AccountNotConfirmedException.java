package com.example.common.exceptions;

public class AccountNotConfirmedException extends RuntimeException {
    public AccountNotConfirmedException(String message) {
        super(message);
    }
}