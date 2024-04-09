package com.example.common.dto.validation.impl;


import com.example.common.dto.validation.Age;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AgeValidator implements ConstraintValidator<Age, LocalDate> {
    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext context) {
        if (birthDate == null) {
            return true;
        }

        LocalDate now = LocalDate.now();
        int age = now.getYear() - birthDate.getYear();

        return age <= 123;
    }
}

