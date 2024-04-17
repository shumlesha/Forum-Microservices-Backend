package com.example.forum.service.impl;

import com.example.common.dto.UserDTO;

import com.example.common.enums.Role;
import com.example.common.exceptions.CategoryHasSubcategoriesException;
import com.example.common.exceptions.ObjectAlreadyExistsException;
import com.example.common.exceptions.ResourceNotFoundException;
import com.example.forum.dto.Category.CreateCategoryModel;
import com.example.forum.dto.Category.EditCategoryModel;
import com.example.forum.models.Category;
import com.example.forum.models.CategoryModerator;
import com.example.forum.repository.CategoryModeratorRepository;
import com.example.forum.repository.CategoryRepository;
import com.example.forum.repository.TopicRepository;
import com.example.forum.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryModeratorRepository categoryModeratorRepository;

    private final TopicRepository topicRepository;

    //private final UserRepository userRepository;
    private final WebClient.Builder webClientBuilder;

    @Override
    @Transactional
    public Category createCategory(UUID authorId, CreateCategoryModel createCategoryModel) {
        if (categoryRepository.existsByName(createCategoryModel.getName())) {
            throw new ObjectAlreadyExistsException("Категория с таким названием уже существует");
        }

        if (createCategoryModel.getParentId() != null && !categoryRepository.existsById(createCategoryModel.getParentId())) {
            throw new ResourceNotFoundException("Категории с таким id не существует: " + createCategoryModel.getParentId());
        }

        //User author = userRepository.findById(authorId)
                //.orElseThrow(() -> new ResourceNotFoundException("Пользователя с таким id не существует: " + authorId));
        UserDTO author = webClientBuilder.build().get()
                .uri("http://users-app/api/internal/users/findById?id=" + authorId)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block();

        Category category = new Category();
        category.setName(createCategoryModel.getName());

        if (createCategoryModel.getParentId() != null) {
            Category parent = categoryRepository.findById(createCategoryModel.getParentId()).orElseThrow(
                    () -> new ResourceNotFoundException("Категории с таким id не сущесвуетвует: " + createCategoryModel.getParentId()));

            if (topicRepository.existsByCategory(parent)) {
                throw new CategoryHasSubcategoriesException("Категории нельзя создать в категории с имеющимися топиками");
            }

            category.setParent(parent);
        }
        category.setAuthorEmail(author.getEmail());

        Category savedCategory = categoryRepository.save(category);

        if (createCategoryModel.getParentId() != null && author.getRoles().contains(Role.ROLE_MODERATOR)) {
            CategoryModerator categoryModerator = new CategoryModerator();
            categoryModerator.setCategory(savedCategory);
            categoryModerator.setModeratorId(author.getId());
            categoryModeratorRepository.save(categoryModerator);
        }

        return savedCategory;
    }

    @Override
    @Transactional
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
    @Transactional
    public void deleteCategory(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Категории с таким id не существует: " + id));

        if (category.getSubcategories().isEmpty()) {
            topicRepository.deleteByCategory(category);
        }

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

    @Override
    public Set<UUID> getSubcategoryIds(UUID categoryId) {
        Set<UUID> subcategoryIds = new HashSet<>();

        Category currentCategory = categoryRepository.findById(categoryId)
                .orElseThrow(()  -> new ResourceNotFoundException("Нет категории с таким id: " + categoryId));
        for (Category subcategory : currentCategory.getSubcategories()) {
            subcategoryIds.add(subcategory.getId());
            subcategoryIds.addAll(getSubcategoryIds(subcategory.getId()));
        }
        return subcategoryIds;
    }

    @Override
    public Category getById(UUID categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(()  -> new ResourceNotFoundException("Нет категории с таким id: " + categoryId));
    }
}
