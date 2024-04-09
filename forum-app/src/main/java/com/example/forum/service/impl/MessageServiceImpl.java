package com.example.forum.service.impl;

import com.example.common.dto.AttachmentDTO;
import com.example.common.dto.UserDTO;
import com.example.common.exceptions.ResourceNotFoundException;
import com.example.common.exceptions.TooManyAttachmentsException;
import com.example.forum.dto.Message.CreateMessageModel;
import com.example.forum.dto.Message.EditMessageModel;
import com.example.forum.dto.Message.MessageFilter;
import com.example.forum.models.Attachment;
import com.example.forum.models.Message;
import com.example.forum.models.Topic;
import com.example.forum.repository.MessageRepository;
import com.example.forum.repository.TopicRepository;
import com.example.forum.service.AttachmentService;
import com.example.forum.service.MessageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final TopicRepository topicRepository;

    private final MessageRepository messageRepository;

    private final AttachmentService attachmentService;

    //private final UserRepository userRepository;
    private final WebClient.Builder webClientBuilder;

    @Override
    @Transactional
    public Message createMessage(UUID topicId,UUID authorId, CreateMessageModel createMessageModel) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Топика с таким id не существует: " + topicId));
        //User author = userRepository.findById(authorId)
                //.orElseThrow(() -> new ResourceNotFoundException("Пользователя с таким id не существует: " + authorId));
        UserDTO author = webClientBuilder.build().get()
                .uri("http://users-app/api/internal/users/findById?id=" + authorId)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block();

        if (createMessageModel.getAttachments().size() > 10) {
            throw new TooManyAttachmentsException("Можно прикрепить не более 10 файлов");
        }

        if (createMessageModel.getAttachments().isEmpty() && createMessageModel.getContent().isEmpty()) {
            throw new IllegalStateException("Некорректное сообщение (пустой текст и нет прикрепленных файлов");
        }

        Message message = new Message();
        message.setContent(createMessageModel.getContent());
        message.setTopic(topic);
        message.setAuthorEmail(author.getEmail());
        Message savedMessage = messageRepository.save(message);
        savedMessage.setAttachments(attachmentService.saveAllAttachs(savedMessage, createMessageModel.getAttachments(), author.getEmail()));

        return savedMessage;
    }

    @Override
    @Transactional
    public void editMessage(UUID id, String email, EditMessageModel editMessageModel) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Сообщения с таким id не существует: " + id));

        if (editMessageModel.getAttachments().size() > 10) {
            throw new TooManyAttachmentsException("Можно прикрепить не более 10 файлов");
        }

        if (editMessageModel.getAttachments().isEmpty() && editMessageModel.getContent().isEmpty()) {
            throw new IllegalStateException("Некорректное сообщение (пустой текст и нет прикрепленных файлов");
        }

        attachmentService.saveAllAttachs(message, editMessageModel.getAttachments(), email);

        message.setContent(editMessageModel.getContent());
        messageRepository.save(message);

    }

    @Override
    @Transactional
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

    @Override
    public Message getById(UUID messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Сообщения с таким id не существует: " + messageId));
    }
}
