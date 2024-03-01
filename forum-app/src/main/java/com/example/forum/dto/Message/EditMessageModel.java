package com.example.forum.dto.Message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditMessageModel {
    @NotBlank(message = "Сообщение не может быть пустым")
    @NotNull(message = "Сообщение не должно быть равным null")
    @Size(min = 1, max = 4500, message = "Сообщение должно содержать от 1 до 4500 символов")
    private String content;
}
