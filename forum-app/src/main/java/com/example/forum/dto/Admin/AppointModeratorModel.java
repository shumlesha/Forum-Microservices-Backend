package com.example.forum.dto.Admin;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointModeratorModel {
    @NotNull(message = "UUID не должен быть равен null")
    private UUID categoryId;
}
