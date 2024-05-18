package com.example.forum.mapper;

import com.example.forum.dto.Topic.FavoriteTopicDTO;
import com.example.forum.models.FavoriteTopic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses={TopicMapper.class})
public interface FavoriteTopicMapper {

    @Mapping(source = "topic", target = "topic")
    FavoriteTopicDTO toDTO(FavoriteTopic favoriteTopic);
}
