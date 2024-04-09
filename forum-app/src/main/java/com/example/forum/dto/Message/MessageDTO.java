package com.example.forum.dto.Message;


import com.example.common.dto.AttachmentDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    private UUID id;
    private String content;
    private UUID topicId;
    private String authorEmail;
    private LocalDateTime createTime;
    private LocalDateTime modifiedDate;
    private List<AttachmentDTO> attachments;
}
