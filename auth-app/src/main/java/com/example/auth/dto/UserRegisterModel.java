package com.example.auth.dto;

import com.example.common.dto.validation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserRegisterModel {

    @NotBlank(message = "Необходимо полное имя", groups = {OnCreate.class, OnUpdate.class})
    @Length(min = 1, message = "Имя должно содержать хотя-бы 1 символ", groups = {OnCreate.class, OnUpdate.class})
    private String fullName;

    @Past(message = "Дата рождения дожна быть прошедшей", groups = {OnCreate.class, OnUpdate.class})
    @Age(groups = {OnCreate.class, OnUpdate.class})
    private LocalDate birthDate;

    @NotBlank(message = "Необходим email", groups = {OnCreate.class, OnUpdate.class})
    @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",
            message = "Необходим валидный email", groups = {OnCreate.class, OnUpdate.class})
    private String email;

    @NotBlank(message = "Необходим номер телефона", groups = {OnCreate.class, OnUpdate.class})
    @PhoneNumber(message = "Некорректный номер телефона", groups = {OnCreate.class, OnUpdate.class})
    private String phoneNumber;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Необходим пароль", groups = {OnCreate.class, OnUpdate.class})
    @Length(min = 6, max = 32, message = "Длина пароля должна быть от 6 до 32 символов", groups = {OnCreate.class, OnUpdate.class})
    @Password(groups = {OnCreate.class})
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Необходимо подтверждение пароля")
    @Length(min = 6, max = 32, message = "Длина пароля должна быть от 6 до 32 символов")
    private String confirmPassword;


}
