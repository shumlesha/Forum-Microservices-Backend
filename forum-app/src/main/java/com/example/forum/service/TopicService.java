package com.example.forum.service;

import com.example.forum.dto.Topic.CreateTopicModel;
import com.example.forum.dto.Topic.EditTopicModel;

import java.util.UUID;

public interface TopicService {

    void createTopic(UUID categoryId, CreateTopicModel createTopicModel);

    void editTopic(UUID id, EditTopicModel editTopicModel);

    void deleteTopic(UUID id);
}
