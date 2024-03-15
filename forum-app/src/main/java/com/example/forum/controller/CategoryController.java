package com.example.forum.controller;



import com.example.forum.dto.Category.CategoryDTO;
import com.example.forum.dto.Category.CreateCategoryModel;
import com.example.forum.dto.Category.EditCategoryModel;
import com.example.forum.mapper.CategoryMapper;
import com.example.forum.models.Category;
import com.example.forum.service.CategoryService;
import com.example.securitylib.JwtUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@CrossOrigin(origins="http://localhost:8989", allowCredentials = "true")
@Tag(name = "Category")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;


    @Operation(summary = "Create category")
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@AuthenticationPrincipal JwtUser jwtUser,
                                            @Validated @RequestBody CreateCategoryModel createCategoryModel) {
        log.info("Создание категории с названием {}", createCategoryModel.getName());
        log.info("Пользователь с ролями: {}", jwtUser.getAuthorities());
        Category createdCategory = categoryService.createCategory(jwtUser.getId(), createCategoryModel);

        return ResponseEntity.ok(categoryMapper.toDTO(createdCategory));
    }

    @Operation(summary = "Update category")
    @PutMapping("/{id}")
    public ResponseEntity<?> editCategory(@PathVariable UUID id, @Validated @RequestBody EditCategoryModel editCategoryModel) {
        log.info("Редактирование категории с id {}", id);
        categoryService.editCategory(id, editCategoryModel);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete category")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable UUID id) {
        log.info("Удаление категории с id {}", id);
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get all categories (hierarchy)")
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        log.info("Получаем список категорий");
        return ResponseEntity.ok(categoryService.getAllCategories().stream()
                .map(categoryMapper::toDTO).collect(Collectors.toList()));
    }

    @Operation(summary = "Get (search) categories by name")
    @GetMapping("/search")
    public ResponseEntity<List<CategoryDTO>> getCategoriesByName(@RequestParam String name) {
        log.info("Получаем список категорий по названию {}", name);
        return ResponseEntity.ok(categoryService.getCategoriesByName(name)
                .stream().map(categoryMapper::toDTO).collect(Collectors.toList()));
    }

}
