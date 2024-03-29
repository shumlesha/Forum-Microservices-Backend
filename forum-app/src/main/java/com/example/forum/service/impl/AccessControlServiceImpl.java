package com.example.forum.service.impl;

import com.example.common.WebClientErrorResponse;
import com.example.common.enums.Role;
import com.example.common.exceptions.WebClientCustomException;
import com.example.common.models.User;
import com.example.forum.models.Category;
import com.example.forum.models.Message;
import com.example.forum.models.Topic;
import com.example.forum.repository.CategoryModeratorRepository;
import com.example.forum.service.AccessControlService;
import com.example.forum.service.CategoryService;
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

    @Override
    public boolean canModerateTopic(UUID userId, UUID topicId) {
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
        Category category = topicService.getById(topicId).getCategory();

        boolean isModerator = user.getRoles().contains(Role.ROLE_MODERATOR);
        boolean hasRules = categoryModeratorRepository.existsByCategoryAndModerator(category, user);

        return isModerator && hasRules || user.getRoles().contains(Role.ROLE_ADMIN);
    }

    @Override
    public boolean isTopicOwner(UUID userId, UUID topicId) {
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
        Topic topic = topicService.getById(topicId);

        return user.getId().equals(topic.getAuthor().getId()) || user.getRoles().contains(Role.ROLE_ADMIN);
    }

    @Override
    public boolean isMessageOwner(UUID userId, UUID messageId) {
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

        Message message = messageService.getById(messageId);

        return user.getId().equals(message.getAuthor().getId());
    }

    @Override
    public boolean canModerateMessage(UUID userId, UUID messageId) {
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
        Message message = messageService.getById(messageId);
        Category category = message.getTopic().getCategory();

        boolean isModerator = user.getRoles().contains(Role.ROLE_MODERATOR);
        boolean hasRules = categoryModeratorRepository.existsByCategoryAndModerator(category, user);

        return isModerator && hasRules || user.getRoles().contains(Role.ROLE_ADMIN);
    }

}
