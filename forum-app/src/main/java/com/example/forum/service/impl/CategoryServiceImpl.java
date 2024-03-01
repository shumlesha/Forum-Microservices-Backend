package com.example.forum.service.impl;

import com.example.common.exceptions.ObjectAlreadyExistsException;
import com.example.common.exceptions.ResourceNotFoundException;
import com.example.forum.dto.Category.CreateCategoryModel;
import com.example.forum.models.Category;
import com.example.forum.repository.CategoryRepository;
import com.example.forum.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

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
}
