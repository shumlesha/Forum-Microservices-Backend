package com.example.forum.dto.Topic;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTopicModel {
    @NotBlank(message = "Название темы не может быть пустым")
    @Length(min = 1, message = "Название темы должно состоять хотя-бы из 1 символа")
    private String name;
}
