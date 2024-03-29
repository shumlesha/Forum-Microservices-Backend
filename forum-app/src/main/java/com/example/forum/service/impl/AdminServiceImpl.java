package com.example.forum.service.impl;


import com.example.common.WebClientErrorResponse;
import com.example.common.enums.Role;
import com.example.common.exceptions.ObjectAlreadyExistsException;
import com.example.common.exceptions.ResourceNotFoundException;
import com.example.common.exceptions.UserIsAlreadyModerator;
import com.example.common.exceptions.WebClientCustomException;
import com.example.common.models.User;
import com.example.forum.dto.Admin.AppointModeratorModel;
import com.example.forum.dto.Admin.RemoveModeratorModel;
import com.example.forum.models.Category;
import com.example.forum.models.CategoryModerator;
import com.example.forum.repository.CategoryModeratorRepository;
import com.example.forum.repository.CategoryRepository;
import com.example.forum.service.AdminService;
import com.example.forum.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final CategoryModeratorRepository categoryModeratorRepository;
    private final CategoryRepository categoryRepository;
    private final WebClient.Builder webClientBuilder;
    private final CategoryService categoryService;
    @Override
    @Transactional
    public void appointModerator(UUID userId, AppointModeratorModel appointModeratorModel) {
        Category category = categoryRepository.findById(appointModeratorModel.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Категории с таким id не существует: " + appointModeratorModel.getCategoryId()));

        User user = webClientBuilder.build().get()
                .uri("http://users-app/api/users/findById?id=" + userId)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.is4xxClientError() || httpStatus.is5xxServerError(), response -> {
                    return response.bodyToMono(WebClientErrorResponse.class).flatMap(errorBody -> {
                        return Mono.error(new WebClientCustomException(errorBody));
                    });
                })
                .bodyToMono(User.class)
                .block();

        if (categoryModeratorRepository.existsByCategoryAndModerator(category, user)) {
            throw new UserIsAlreadyModerator("Пользователь уже модерирует эту категорию");
        }

        // TODO: Получить id всех подкатегорий и назначить модератором
        Set<UUID> subCategories = categoryService.getSubcategoryIds(category.getId());
        subCategories.add(category.getId());

        for (UUID uuid : subCategories) {
            Category currentCategory = categoryRepository.findById(uuid).orElseThrow();

            if (!categoryModeratorRepository.existsByCategoryAndModerator(currentCategory, user)) {
                CategoryModerator categoryModerator = new CategoryModerator();
                categoryModerator.setCategory(currentCategory);
                categoryModerator.setModerator(user);
                categoryModeratorRepository.save(categoryModerator);
            }
        }

        if (!user.getRoles().contains(Role.ROLE_MODERATOR)) {
            user.setRoles(Set.of(Role.ROLE_MODERATOR, Role.ROLE_USER));
            webClientBuilder.build().post()
                  .uri("http://users-app/api/users/saveUser")
                  .bodyValue(user)
                  .retrieve()
                  .bodyToMono(User.class).block();
        }
        /*CategoryModerator categoryModerator = new CategoryModerator();
        categoryModerator.setCategory(category);
        categoryModerator.setModerator(user);*/
    }

    @Override
    @Transactional
    public void removeModerator(UUID userId, RemoveModeratorModel removeModeratorModel) {
        Category category = categoryRepository.findById(removeModeratorModel.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Категории с таким id не существует: " + removeModeratorModel.getCategoryId()));

        User user = webClientBuilder.build().get()
                .uri("http://users-app/api/users/findById?id=" + userId)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.is4xxClientError() || httpStatus.is5xxServerError(), response -> {
                    return response.bodyToMono(WebClientErrorResponse.class).flatMap(errorBody -> {
                        return Mono.error(new WebClientCustomException(errorBody));
                    });
                })
                .bodyToMono(User.class)
                .block();

        if (!categoryModeratorRepository.existsByCategoryAndModerator(category, user)) {
            throw new UserIsAlreadyModerator("Пользователь не модерирует эту категорию");
        }

        Set<UUID> subCategories = categoryService.getSubcategoryIds(category.getId());
        subCategories.add(category.getId());

        for (UUID uuid : subCategories) {
            Category currentCategory = categoryRepository.findById(uuid).orElseThrow();
            categoryModeratorRepository.deleteByCategoryAndModerator(currentCategory, user);
        }

        if (!categoryModeratorRepository.existsByModerator(user)) {
            user.setRoles(Set.of(Role.ROLE_USER));
            webClientBuilder.build().post()
                 .uri("http://users-app/api/users/saveUser")
                 .bodyValue(user)
                 .retrieve()
                 .bodyToMono(User.class).block();
        }
    }
}
