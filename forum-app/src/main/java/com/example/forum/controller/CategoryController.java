package com.example.forum.controller;


import com.example.forum.dto.Category.CreateCategoryModel;
import com.example.forum.dto.Category.EditCategoryModel;
import com.example.forum.models.Category;
import com.example.forum.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<?> createCategory(@Validated @RequestBody CreateCategoryModel createCategoryModel) {
        log.info("Создание категории с названием {}", createCategoryModel.getName());
        categoryService.createCategory(createCategoryModel);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editCategory(@PathVariable UUID id, @Validated @RequestBody EditCategoryModel editCategoryModel) {
        log.info("Редактирование категории с id {}", id);
        categoryService.editCategory(id, editCategoryModel);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable UUID id) {
        log.info("Удаление категории с id {}", id);
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        log.info("Получаем список категорий");
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Category>> getCategoriesByName(@RequestParam String name) {
        log.info("Получаем список категорий по названию {}", name);
        return ResponseEntity.ok(categoryService.getCategoriesByName(name));
    }

}
