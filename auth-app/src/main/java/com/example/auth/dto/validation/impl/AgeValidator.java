package com.example.auth.dto.validation.impl;


import com.example.auth.dto.validation.Age;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class AgeValidator implements ConstraintValidator<Age, LocalDateTime> {
    @Override
    public boolean isValid(LocalDateTime birthDate, ConstraintValidatorContext context) {
        if (birthDate == null) {
            return true;
        }

        LocalDateTime now = LocalDateTime.now();
        int age = now.getYear() - birthDate.getYear();

        return age <= 123;
    }
}

