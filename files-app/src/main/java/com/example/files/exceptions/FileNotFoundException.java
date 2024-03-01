package com.example.files.exceptions;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class FileNotFoundException extends ResponseStatusException {

    public FileNotFoundException(HttpStatusCode status, String reason) {
        super(status, reason);
    }
}
