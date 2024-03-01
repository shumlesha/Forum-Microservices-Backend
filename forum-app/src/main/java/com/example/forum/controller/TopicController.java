package com.example.forum.controller;


import com.example.forum.dto.Topic.CreateTopicModel;
import com.example.forum.dto.Topic.EditTopicModel;
import com.example.forum.models.Category;
import com.example.forum.models.Topic;
import com.example.forum.service.TopicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping
    public ResponseEntity<Page<Topic>> getAllTopics(@PageableDefault(sort = "createTime",
            direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(topicService.getAllTopics(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Topic>> getTopicsByName(@RequestParam String name) {
        return ResponseEntity.ok(topicService.getTopicsByName(name));
    }
}
