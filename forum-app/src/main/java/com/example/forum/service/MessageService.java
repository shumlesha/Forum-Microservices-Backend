package com.example.forum.service;

import com.example.forum.dto.Message.CreateMessageModel;
import com.example.forum.dto.Message.EditMessageModel;
import com.example.forum.models.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    void createMessage(UUID topicId, CreateMessageModel createMessageModel);

    void editMessage(UUID id, EditMessageModel editMessageModel);

    void deleteMessage(UUID id);

    Page<Message> getMessages(UUID topicId, Pageable pageable);

    List<Message> getMessagesByContent(String content);
}
