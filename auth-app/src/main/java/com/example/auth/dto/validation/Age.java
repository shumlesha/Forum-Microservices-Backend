package com.example.auth.dto.validation;


import com.example.auth.dto.validation.impl.AgeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD, METHOD, PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = AgeValidator.class)
public @interface Age {
    String message() default "Вы не можете быть старше 123 лет";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
