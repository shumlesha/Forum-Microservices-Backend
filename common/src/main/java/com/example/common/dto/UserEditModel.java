package com.example.common.dto;

import com.example.common.dto.validation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEditModel {
    @NotBlank(message = "Необходимо полное имя")
    @Length(min = 1, message = "Имя должно содержать хотя-бы 1 символ")
    private String fullName;

    @Past(message = "Дата рождения дожна быть прошедшей")
    @Age(groups = {OnCreate.class, OnUpdate.class})
    private LocalDate birthDate;

    @NotBlank(message = "Необходисм номер телефона")
    @PhoneNumber(message = "Некорректный номер телефона")
    private String phoneNumber;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Необходим пароль")
    @Length(min = 6, max = 32, message = "Длина пароля должна быть от 6 до 32 символов")
    @Password(groups = {OnCreate.class})
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Необходимо подтверждение пароля")
    @Length(min = 6, max = 32, message = "Длина пароля должна быть от 6 до 32 символов")
    private String confirmPassword;
}
