package com.example.forum.dto.Topic;


import com.example.forum.dto.Message.MessageDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicDTO {
    private UUID id;
    private String name;
    private UUID categoryId;
    private String authorEmail;
    private List<MessageDTO> messages;
    private LocalDateTime createTime;
    private LocalDateTime modifiedDate;
}
