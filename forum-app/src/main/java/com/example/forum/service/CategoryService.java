package com.example.forum.service;

import com.example.forum.dto.Category.CreateCategoryModel;
import com.example.forum.dto.Category.EditCategoryModel;
import com.example.forum.models.Category;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface CategoryService {
    void createCategory(CreateCategoryModel createCategoryModel);

    void editCategory(UUID id, EditCategoryModel editCategoryModel);

    void deleteCategory(UUID id);

    List<Category> getAllCategories();

    List<Category> getCategoriesByName(String name);

    Set<UUID> getSubcategoryIds(UUID categoryId);
}
