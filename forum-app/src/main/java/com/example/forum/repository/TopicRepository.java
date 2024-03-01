package com.example.forum.repository;

import com.example.forum.models.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TopicRepository extends JpaRepository<Topic, UUID> {
    boolean existsByName(String name);

    List<Topic> findByNameContainingIgnoreCase(String name);
}
