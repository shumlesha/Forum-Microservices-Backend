package com.example.forum.service.impl;

import com.example.common.exceptions.ResourceNotFoundException;
import com.example.forum.dto.Message.CreateMessageModel;
import com.example.forum.dto.Message.EditMessageModel;
import com.example.forum.dto.Message.MessageFilter;
import com.example.forum.models.Message;
import com.example.forum.models.Topic;
import com.example.forum.repository.MessageRepository;
import com.example.forum.repository.TopicRepository;
import com.example.forum.service.MessageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final TopicRepository topicRepository;

    private final MessageRepository messageRepository;


    @Override
    public void createMessage(UUID topicId, CreateMessageModel createMessageModel) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Топика с таким id не существует: " + topicId));

        Message message = new Message();
        message.setContent(createMessageModel.getContent());
        message.setTopic(topic);
        messageRepository.save(message);
    }

    @Override
    public void editMessage(UUID id, EditMessageModel editMessageModel) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Сообщения с таким id не существует: " + id));

        message.setContent(editMessageModel.getContent());
        messageRepository.save(message);

    }

    @Override
    public void deleteMessage(UUID id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Сообщения с таким id не существует: " + id));

        messageRepository.delete(message);
    }

    @Override
    public Page<Message> getMessages(UUID topicId, Pageable pageable) {
        return messageRepository.findByTopicId(topicId, pageable);
    }

    @Override
    public List<Message> getMessagesByContent(String content) {
        return messageRepository.findByContentContainingIgnoreCase(content);
    }

    @Override
    public List<Message> searchMessages(MessageFilter messageFilter) {
        log.info("Поиск сообщений начат");
        return messageRepository.findByFilter(messageFilter);
    }
}