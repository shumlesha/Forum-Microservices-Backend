package com.example.forum.mapper;


import com.example.forum.dto.Topic.TopicDTO;
import com.example.forum.models.Topic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MessageMapper.class})
public interface TopicMapper {
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(target = "messages", source = "messages")
    @Mapping(source = "author.email", target = "authorEmail")
    TopicDTO toDTO(Topic topic);
}
