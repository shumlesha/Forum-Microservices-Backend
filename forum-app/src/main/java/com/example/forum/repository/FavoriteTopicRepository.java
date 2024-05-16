package com.example.forum.repository;

import com.example.forum.models.FavoriteTopic;
import com.example.forum.models.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FavoriteTopicRepository extends JpaRepository<FavoriteTopic, UUID> {
    boolean existsByTopicAndOwnerEmail(Topic topic, String email);

    Optional<FavoriteTopic> findByTopicAndOwnerEmail(Topic topic, String email);

    Page<FavoriteTopic> findByOwnerEmail(String email, Pageable pageable);

    List<FavoriteTopic> findByTopic(Topic topic);
}
