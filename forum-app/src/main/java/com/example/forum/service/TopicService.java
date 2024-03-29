package com.example.forum.service;

import com.example.forum.dto.Topic.CreateTopicModel;
import com.example.forum.dto.Topic.EditTopicModel;
import com.example.forum.models.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface TopicService {

    Topic createTopic(UUID authorId, UUID categoryId, CreateTopicModel createTopicModel);

    void editTopic(UUID id, EditTopicModel editTopicModel);

    void deleteTopic(UUID id);

    Page<Topic> getAllTopics(Pageable pageable);

    List<Topic> getTopicsByName(String name);

    Topic getById(UUID topicId);
}
