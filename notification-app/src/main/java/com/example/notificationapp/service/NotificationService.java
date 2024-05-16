package com.example.notificationapp.service;

import com.example.common.dto.notifications.NotificationDTO;
import com.example.notificationapp.models.Notification;
import com.example.securitylib.JwtUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.nio.channels.FileChannel;

public interface NotificationService {
    void save(String receiver, NotificationDTO notificationDTO);

    Page<Notification> getNotifications(JwtUser jwtUser, String queryText, Pageable pageable);

    Long getNonReadNotificationsCount(JwtUser jwtUser);
}

