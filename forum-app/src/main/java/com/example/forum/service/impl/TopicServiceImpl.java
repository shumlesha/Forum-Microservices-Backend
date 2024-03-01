package com.example.forum.service.impl;

import com.example.common.exceptions.CategoryHasSubcategoriesException;
import com.example.common.exceptions.ObjectAlreadyExistsException;
import com.example.common.exceptions.ResourceNotFoundException;
import com.example.forum.dto.Topic.CreateTopicModel;
import com.example.forum.dto.Topic.EditTopicModel;
import com.example.forum.models.Category;
import com.example.forum.models.Topic;
import com.example.forum.repository.CategoryRepository;
import com.example.forum.repository.TopicRepository;
import com.example.forum.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {

    private final CategoryRepository categoryRepository;
    private final TopicRepository topicRepository;

    @Override
    public void createTopic(UUID categoryId, CreateTopicModel createTopicModel) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Категории с таким id не найдено: " + categoryId));
        if (!category.getSubcategories().isEmpty()) {
            throw new CategoryHasSubcategoriesException("Топики можно создавать только в категориях нижнего уровня");
        }

        if (categoryRepository.existsByName(createTopicModel.getName())) {
            throw new ObjectAlreadyExistsException("Топик с таким названием уже существует: " + createTopicModel.getName());
        }

        Topic topic = new Topic();
        topic.setName(createTopicModel.getName());
        topic.setCategory(category);

        topicRepository.save(topic);
    }

    @Override
    public void editTopic(UUID id, EditTopicModel editTopicModel) {
        if (topicRepository.existsByName(editTopicModel.getName())) {
            throw new ObjectAlreadyExistsException("Топик с таким названием уже существует: " + editTopicModel.getName());
        }

        Topic topic = topicRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Топика с таким id не существует: " + id));

        topic.setName(editTopicModel.getName());
        topicRepository.save(topic);
    }

    @Override
    public void deleteTopic(UUID id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Топика с таким id не существует: " + id));
        topicRepository.delete(topic);
    }
}
