package com.example.common.dto;

import com.example.common.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    UUID id;
    String fullName;
    LocalDate birthDate;
    String email;
    String phoneNumber;
    String password;
    String confirmPassword;
    Set<Role> roles;
    boolean confirmed;
    boolean banned;
}