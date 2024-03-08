package com.example.auth.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserLoginModel {

    @NotBlank(message = "Необходим email")
    @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", message = "Неверный email")
    private String email;

    @NotBlank(message = "Необходим пароль")
    @Length(min = 1, message = "Пароль должен содержать хотя-бы 1 символ")
    private String password;
}

