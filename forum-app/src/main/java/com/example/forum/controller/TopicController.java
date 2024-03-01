package com.example.forum.controller;


import com.example.forum.dto.Topic.CreateTopicModel;
import com.example.forum.dto.Topic.EditTopicModel;
import com.example.forum.service.TopicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
public class TopicController {
    private final TopicService topicService;

    @PostMapping("/{categoryId}")
    public ResponseEntity<?> createTopic(@PathVariable UUID categoryId,
                                         @Validated @RequestBody CreateTopicModel createTopicModel) {
        topicService.createTopic(categoryId, createTopicModel);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editTopic(@PathVariable UUID id,
                                         @Validated @RequestBody EditTopicModel editTopicModel) {
        topicService.editTopic(id, editTopicModel);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTopic(@PathVariable UUID id) {
        topicService.deleteTopic(id);
        return ResponseEntity.ok().build();
    }
}
