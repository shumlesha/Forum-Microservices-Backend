package com.example.files;

import com.example.files.exceptions.FileNotFoundException;
import com.example.files.exceptions.FileUploadException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {


    @ExceptionHandler(FileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFileNotFound(FileNotFoundException exception) {
        log.error(exception.getMessage(), exception);
        return new ErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(FileUploadException.class)
    public ErrorResponse handleFileUpload(FileUploadException exception) {
        log.error(exception.getMessage(), exception);
        return new ErrorResponse(exception.getStatusCode(), exception.getMessage());
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception e) {
        log.error(e.getMessage(), e.getLocalizedMessage());
        return new ErrorResponse(HttpStatus.BAD_REQUEST, "Internal error");
    }

}
