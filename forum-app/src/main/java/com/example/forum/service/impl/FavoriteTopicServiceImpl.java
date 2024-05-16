package com.example.forum.service.impl;


import com.example.common.exceptions.ObjectAlreadyExistsException;
import com.example.common.exceptions.ResourceNotFoundException;
import com.example.forum.models.FavoriteTopic;
import com.example.forum.models.Topic;
import com.example.forum.repository.FavoriteTopicRepository;
import com.example.forum.repository.TopicRepository;
import com.example.forum.service.FavoriteTopicService;
import com.example.securitylib.JwtUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class FavoriteTopicServiceImpl implements FavoriteTopicService {

    private final FavoriteTopicRepository favoriteTopicRepository;
    private final TopicRepository topicRepository;

    @Override
    @Transactional
    public void addFavoriteTopic(UUID topicId, JwtUser jwtUser) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Топика с таким id не существует: " + topicId));

        if (favoriteTopicRepository.existsByTopicAndOwnerEmail(topic, jwtUser.getEmail())) {
            throw new ObjectAlreadyExistsException("Вы уже добавили этот топик в избранное");
        }

        FavoriteTopic favoriteTopic = new FavoriteTopic();
        favoriteTopic.setTopic(topic);
        favoriteTopic.setOwnerEmail(jwtUser.getEmail());

        favoriteTopicRepository.save(favoriteTopic);
    }


    @Override
    @Transactional
    public void deleteFavoriteTopic(UUID topicId, JwtUser jwtUser) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Топика с таким id не существует: " + topicId));

        FavoriteTopic favoriteTopic = favoriteTopicRepository.findByTopicAndOwnerEmail(topic, jwtUser.getEmail())
                        .orElseThrow(() -> new ResourceNotFoundException("Этого топика нет в ваших избранных"));

        favoriteTopicRepository.delete(favoriteTopic);
    }

    @Override
    public Page<FavoriteTopic> getFavoriteTopics(JwtUser jwtUser, Pageable pageable) {
        return favoriteTopicRepository.findByOwnerEmail(jwtUser.getEmail(), pageable);
    }
}
