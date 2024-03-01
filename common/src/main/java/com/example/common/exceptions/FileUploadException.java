package com.example.common.exceptions;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class FileUploadException extends ResponseStatusException {
    public FileUploadException(HttpStatusCode status, String reason) { super(status, reason); }
}
