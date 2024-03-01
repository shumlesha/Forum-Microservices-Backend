package com.example.forum.dto.Message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchMessageRequest {
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private UUID topicId;
    private UUID categoryId;
}
