package com.example.notificationapp.mapper;

import com.example.notificationapp.dto.NotificationUserDTO;
import com.example.notificationapp.models.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    @Mapping(source = "read", target = "read")
    NotificationUserDTO toDto(Notification notification);
}
