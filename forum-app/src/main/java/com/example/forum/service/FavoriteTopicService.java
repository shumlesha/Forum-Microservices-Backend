package com.example.forum.service;


import com.example.forum.models.FavoriteTopic;
import com.example.securitylib.JwtUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface FavoriteTopicService {
    void addFavoriteTopic(UUID topicId, JwtUser jwtUser);

    void deleteFavoriteTopic(UUID topicId, JwtUser jwtUser);

    Page<FavoriteTopic> getFavoriteTopics(JwtUser jwtUser, Pageable pageable);
}
