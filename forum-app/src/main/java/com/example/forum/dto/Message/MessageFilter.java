package com.example.forum.dto.Message;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

public record MessageFilter(
        String content,

        @Schema(pattern = "yyyy-MM-dd'T'HH:mm", example="yyyy-MM-dd'T'HH:mm")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime startDate,

        @Schema(pattern = "yyyy-MM-dd'T'HH:mm", example="yyyy-MM-dd'T'HH:mm")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime endDate,
        String authorLogin,
        UUID topicId,
        UUID categoryId
) {}
