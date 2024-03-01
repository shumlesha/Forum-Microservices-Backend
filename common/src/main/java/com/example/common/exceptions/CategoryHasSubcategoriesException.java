package com.example.common.exceptions;

public class CategoryHasSubcategoriesException extends RuntimeException {
    public CategoryHasSubcategoriesException(String message) {
        super(message);
    }
}
