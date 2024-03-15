package com.example.forum.dto.Category;

import com.example.forum.dto.Topic.TopicDTO;
import com.example.forum.models.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private UUID id;
    private String name;
    private UUID parentId;
    private List<CategoryDTO> subcategories;
    private List<TopicDTO> topics;
    private String authorEmail;
    private LocalDateTime createTime;
    private LocalDateTime modifiedDate;
}
