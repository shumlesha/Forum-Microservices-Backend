package com.example.notificationapp.mapper;

import com.example.notificationapp.dto.NotificationUserDTO;
import com.example.notificationapp.models.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationUserDTO toDto(Notification notification);
}
