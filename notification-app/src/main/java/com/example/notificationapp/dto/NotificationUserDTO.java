package com.example.notificationapp.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationUserDTO {
    private UUID id;
    private String topic;
    private String content;
    private String receiverEmail;
    private LocalDateTime createTime;
    private boolean isRead;
}
