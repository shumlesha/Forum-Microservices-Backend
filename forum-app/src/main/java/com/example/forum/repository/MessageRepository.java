package com.example.forum.repository;

import com.example.forum.models.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface MessageRepository extends JpaRepository<Message, UUID>,
        FilterMessageRepository{
    Page<Message> findByTopicId(UUID topicId, Pageable pageable);

    List<Message> findByContentContainingIgnoreCase(String content);
}
