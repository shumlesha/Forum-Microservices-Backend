package com.example.forum.mapper;

import com.example.forum.dto.Message.MessageDTO;
import com.example.forum.models.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    @Mapping(source = "topic.id", target = "topicId")
    @Mapping(source = "authorEmail", target = "authorEmail")
    MessageDTO toDTO(Message message);
}
