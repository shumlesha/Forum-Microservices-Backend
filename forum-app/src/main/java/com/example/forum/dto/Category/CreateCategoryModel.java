package com.example.forum.dto.Category;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCategoryModel {

    @NotBlank(message = "Название категории не может быть пустым")
    @Length(min = 1, message = "Название категории должно состоять хотя-бы из 1 символа")
    private String name;

    private UUID parentId;
}
