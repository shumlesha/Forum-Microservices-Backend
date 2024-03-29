package com.example.forum.service;

import com.example.forum.dto.Message.CreateMessageModel;
import com.example.forum.dto.Message.EditMessageModel;
import com.example.forum.dto.Message.MessageFilter;
import com.example.forum.models.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message createMessage(UUID topicId, UUID authorId, CreateMessageModel createMessageModel);

    void editMessage(UUID id, String email, EditMessageModel editMessageModel);

    void deleteMessage(UUID id);

    Page<Message> getMessages(UUID topicId, Pageable pageable);

    List<Message> getMessagesByContent(String content);

    List<Message> searchMessages(MessageFilter messageFilter);

    Message getById(UUID messageId);
}
