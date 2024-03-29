package com.example.common.exceptions;

public class UserIsAlreadyModerator extends RuntimeException{
    public UserIsAlreadyModerator(String message) {
        super(message);
    }
}
