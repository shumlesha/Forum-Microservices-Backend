package com.example.forum.service.impl;

import com.example.common.exceptions.ObjectAlreadyExistsException;
import com.example.common.exceptions.ResourceNotFoundException;
import com.example.forum.dto.Category.CreateCategoryModel;
import com.example.forum.dto.Category.EditCategoryModel;
import com.example.forum.models.Category;
import com.example.forum.repository.CategoryRepository;
import com.example.forum.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public void createCategory(CreateCategoryModel createCategoryModel) {
        if (categoryRepository.existsByName(createCategoryModel.getName())) {
            throw new ObjectAlreadyExistsException("Категория с таким названием уже существует");
        }

        if (createCategoryModel.getParentId() != null && !categoryRepository.existsById(createCategoryModel.getParentId())) {
            throw new ResourceNotFoundException("Категории с таким id не существует: " + createCategoryModel.getParentId());
        }

        Category category = new Category();
        category.setName(createCategoryModel.getName());

        if (createCategoryModel.getParentId() != null) {
            Category parent = categoryRepository.findById(createCategoryModel.getParentId()).orElseThrow(
                    () -> new ResourceNotFoundException("Категория с таким id не сущесвуетвует: " + createCategoryModel.getParentId()));

            category.setParent(parent);
        }
        categoryRepository.save(category);
    }

    @Override
    public void editCategory(UUID id, EditCategoryModel editCategoryModel) {
        if (categoryRepository.existsByName(editCategoryModel.getName())) {
            throw new ObjectAlreadyExistsException("Категория с таким названием уже существует");
        }

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Категории с таким id не существует: " + id));

        category.setName(editCategoryModel.getName());
        categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Категории с таким id не существует: " + id));
        categoryRepository.delete(category);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findByParentIsNullOrderByNameAsc();
    }

    @Override
    public List<Category> getCategoriesByName(String name) {
        return categoryRepository.findByNameContainingIgnoreCase(name);
    }
}
