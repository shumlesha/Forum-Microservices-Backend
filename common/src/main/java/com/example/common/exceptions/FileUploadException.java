package com.example.common.exceptions;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class FileUploadException extends RuntimeException {
    public FileUploadException(String message) { super(message); }
}
