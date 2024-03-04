package com.example.forum.dto.Message;

import java.time.LocalDateTime;
import java.util.UUID;

public record MessageFilter(
        String content,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String authorLogin,
        UUID topicId,
        UUID categoryId
) {}
