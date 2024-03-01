package com.example.forum.service;

import com.example.forum.dto.Category.CreateCategoryModel;
import com.example.forum.dto.Category.EditCategoryModel;

import java.util.UUID;

public interface CategoryService {
    void createCategory(CreateCategoryModel createCategoryModel);

    void editCategory(UUID id, EditCategoryModel editCategoryModel);

    void deleteCategory(UUID id);
}
