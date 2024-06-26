package com.example.forum.service.impl;

import com.example.common.WebClientErrorResponse;
import com.example.common.dto.UserDTO;
import com.example.common.enums.Role;
import com.example.common.exceptions.ResourceNotFoundException;
import com.example.common.exceptions.WebClientCustomException;
import com.example.forum.models.Category;
import com.example.forum.models.Message;
import com.example.forum.models.Topic;
import com.example.forum.repository.CategoryModeratorRepository;
import com.example.forum.repository.CategoryRepository;
import com.example.forum.service.AccessControlService;
import com.example.forum.service.MessageService;
import com.example.forum.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service("acsi")
@RequiredArgsConstructor
public class AccessControlServiceImpl implements AccessControlService {
    private final WebClient.Builder webClientBuilder;
    private final TopicService topicService;
    private final CategoryModeratorRepository categoryModeratorRepository;
    private final MessageService messageService;
    private final CategoryRepository categoryRepository;


    @Override
    public boolean canModerateCategory(UUID userId, UUID parentId) {
        UserDTO user = webClientBuilder.build().get()
                .uri("http://users-app/api/internal/users/findById?id=" + userId)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.is4xxClientError() || httpStatus.is5xxServerError(), response -> {
                    return response.bodyToMono(WebClientErrorResponse.class).flatMap(errorBody -> {
                        return Mono.error(new WebClientCustomException(errorBody));
                    });
                })
                .bodyToMono(UserDTO.class)
                .block();

        boolean isModerator = false;
        if (parentId != null) {
            Category category = categoryRepository.findById(parentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Категории с таким id не существует " + parentId));

            boolean hasRules = categoryModeratorRepository.existsByCategoryAndModeratorId(category, user.getId());
            isModerator = user.getRoles().contains(Role.ROLE_MODERATOR) && (hasRules);
        }

        return user.getRoles().contains(Role.ROLE_ADMIN) || isModerator;

    }

    @Override
    public boolean canModerateTopic(UUID userId, UUID topicId) {
        UserDTO user = webClientBuilder.build().get()
                .uri("http://users-app/api/internal/users/findById?id=" + userId)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.is4xxClientError() || httpStatus.is5xxServerError(), response -> {
                    return response.bodyToMono(WebClientErrorResponse.class).flatMap(errorBody -> {
                        return Mono.error(new WebClientCustomException(errorBody));
                    });
                })
                .bodyToMono(UserDTO.class)
                .block();
        Category category = topicService.getById(topicId).getCategory();

        boolean isModerator = user.getRoles().contains(Role.ROLE_MODERATOR);
        boolean hasRules = categoryModeratorRepository.existsByCategoryAndModeratorId(category, user.getId());

        return isModerator && hasRules || user.getRoles().contains(Role.ROLE_ADMIN);
    }

    @Override
    public boolean isTopicOwner(UUID userId, UUID topicId) {
        UserDTO user = webClientBuilder.build().get()
                .uri("http://users-app/api/internal/users/findById?id=" + userId)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.is4xxClientError() || httpStatus.is5xxServerError(), response -> {
                    return response.bodyToMono(WebClientErrorResponse.class).flatMap(errorBody -> {
                        return Mono.error(new WebClientCustomException(errorBody));
                    });
                })
                .bodyToMono(UserDTO.class)
                .block();
        Topic topic = topicService.getById(topicId);

        return user.getEmail().equals(topic.getAuthorEmail()) || user.getRoles().contains(Role.ROLE_ADMIN);
    }

    @Override
    public boolean isMessageOwner(UUID userId, UUID messageId) {
        UserDTO user = webClientBuilder.build().get()
                .uri("http://users-app/api/internal/users/findById?id=" + userId)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.is4xxClientError() || httpStatus.is5xxServerError(), response -> {
                    return response.bodyToMono(WebClientErrorResponse.class).flatMap(errorBody -> {
                        return Mono.error(new WebClientCustomException(errorBody));
                    });
                })
                .bodyToMono(UserDTO.class)
                .block();

        Message message = messageService.getById(messageId);
        Category category = message.getTopic().getCategory();

        boolean isModerator = user.getRoles().contains(Role.ROLE_MODERATOR);
        boolean hasRules = categoryModeratorRepository.existsByCategoryAndModeratorId(category, user.getId());

        boolean isHigherPerson =  isModerator && hasRules || user.getRoles().contains(Role.ROLE_ADMIN);

        return user.getEmail().equals(message.getAuthorEmail()) || isHigherPerson;
    }

    @Override
    public boolean canModerateMessage(UUID userId, UUID messageId) {
        UserDTO user = webClientBuilder.build().get()
                .uri("http://users-app/api/internal/users/findById?id=" + userId)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.is4xxClientError() || httpStatus.is5xxServerError(), response -> {
                    return response.bodyToMono(WebClientErrorResponse.class).flatMap(errorBody -> {
                        return Mono.error(new WebClientCustomException(errorBody));
                    });
                })
                .bodyToMono(UserDTO.class)
                .block();
        Message message = messageService.getById(messageId);
        Category category = message.getTopic().getCategory();

        boolean isModerator = user.getRoles().contains(Role.ROLE_MODERATOR);
        boolean hasRules = categoryModeratorRepository.existsByCategoryAndModeratorId(category, user.getId());

        return isModerator && hasRules || user.getRoles().contains(Role.ROLE_ADMIN);
    }

}
