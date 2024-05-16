package com.example.forum.dto.Topic;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteTopicDTO {
    private UUID id;
    private TopicDTO topicDTO;
    private String ownerEmail;
    private LocalDateTime createTime;
}
