package com.example.forum.dto.Message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditMessageModel {

    @NotNull(message = "Сообщение не должно быть равным null")
    @Size(max = 4500, message = "Сообщение должно содержать от 0 до 4500 символов")
    private String content;

    private List<UUID> attachments;
}
