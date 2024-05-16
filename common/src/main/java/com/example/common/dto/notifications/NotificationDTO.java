package com.example.common.dto.notifications;


import lombok.*;
import org.jetbrains.annotations.TestOnly;

import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {
    private String topic;
    private String content;
    private String receiverEmail;
    private Set<NotificationChannel> channels;
    private boolean saveInHistory;
    private Object payload;
    private NotificationType notificationType;
}
