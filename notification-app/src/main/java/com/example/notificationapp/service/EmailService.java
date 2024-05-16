package com.example.notificationapp.service;

import com.example.common.dto.UserDTO;
import com.example.common.dto.notifications.NotificationDTO;


public interface EmailService {
    void sendPersonalEmail(String email, NotificationDTO notificationDTO);
}
