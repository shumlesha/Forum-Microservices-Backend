package com.example.auth.kafka.service;

import com.example.common.dto.notifications.NotificationDTO;

public interface KafkaSenderService {
    void send(NotificationDTO notificationDTO);
}
