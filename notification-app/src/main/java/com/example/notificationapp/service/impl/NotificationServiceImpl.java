package com.example.notificationapp.service.impl;

import com.example.common.dto.notifications.NotificationDTO;
import com.example.notificationapp.models.Notification;
import com.example.notificationapp.repository.NotificationRepository;
import com.example.notificationapp.service.NotificationService;
import com.example.securitylib.JwtUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.webmvc.api.MultipleOpenApiWebMvcResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public void save(String receiver, NotificationDTO notificationDTO) {

        Notification notification = new Notification();
        notification.setTopic(notificationDTO.getTopic());
        notification.setContent(notificationDTO.getContent());
        notification.setReceiverEmail(receiver);
        notification.setRead(false);

        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public Page<Notification> getNotifications(JwtUser jwtUser, String queryText, Pageable pageable) {
        Page<Notification> notifications;
        log.info(pageable.toString());
        if (queryText == null || queryText.isBlank()) {
            notifications = notificationRepository.findByReceiverEmailOrderByIsReadAsc(jwtUser.getEmail(), pageable);
        }
        else {
            notifications = notificationRepository.findByReceiverEmailAndTopicContainingIgnoreCaseOrContentContainingIgnoreCase(jwtUser.getEmail(), queryText, pageable);
        }

        Set<UUID> ids = notifications.getContent().stream().map(Notification::getId).collect(Collectors.toSet());
        notificationRepository.readNotifications(ids);

        return notifications;
    }

    @Override
    public Long getNonReadNotificationsCount(JwtUser jwtUser) {
        return notificationRepository.countByReceiverEmailAndIsReadFalse(jwtUser.getEmail());
    }

}
