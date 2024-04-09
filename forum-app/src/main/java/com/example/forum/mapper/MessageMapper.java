package com.example.forum.mapper;

import com.example.forum.dto.Message.MessageDTO;
import com.example.forum.models.Attachment;
import com.example.forum.models.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses={AttachmentMapper.class})
public interface MessageMapper {
    @Mapping(source = "topic.id", target = "topicId")
    @Mapping(source = "authorEmail", target = "authorEmail")
    @Mapping(target = "attachments", source = "attachments")
    MessageDTO toDTO(Message message);

}
