package com.example.notificationapp.service;

import com.example.common.dto.notifications.NotificationDTO;
import com.google.gson.Gson;


public interface KafkaNotificationService {

    void handle(NotificationDTO notificationDTO, Gson gson);
}
