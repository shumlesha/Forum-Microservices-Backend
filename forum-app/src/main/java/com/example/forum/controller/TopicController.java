package com.example.forum.controller;


import com.example.forum.dto.Topic.CreateTopicModel;
import com.example.forum.dto.Topic.EditTopicModel;
import com.example.forum.dto.Topic.TopicDTO;
import com.example.forum.mapper.TopicMapper;
import com.example.forum.models.Topic;
import com.example.forum.service.AccessControlService;
import com.example.forum.service.TopicService;
import com.example.securitylib.JwtUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
@Tag(name = "Topic")
public class TopicController {
    private final TopicService topicService;
    private final TopicMapper topicMapper;
    private final AccessControlService accessControlService;

    @Operation(summary = "Create topic in concrete category by id")
    @PostMapping("/{categoryId}")
    public ResponseEntity<TopicDTO> createTopic(@PathVariable UUID categoryId,
                                         @Validated @RequestBody CreateTopicModel createTopicModel,
                                         @AuthenticationPrincipal JwtUser jwtUser) {
        Topic createdTopic = topicService.createTopic(jwtUser.getId(), categoryId, createTopicModel);
        return ResponseEntity.ok(topicMapper.toDTO(createdTopic));
    }

    @Operation(summary = "Edit topic by its id")
    @PutMapping("/{id}")
    @PreAuthorize("@acsi.isTopicOwner(#jwtUser.id, #id)")
    public ResponseEntity<?> editTopic(@PathVariable UUID id,
                                       @Validated @RequestBody EditTopicModel editTopicModel,
                                       @AuthenticationPrincipal JwtUser jwtUser) {
        topicService.editTopic(id, editTopicModel);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete topic by its id")
    @DeleteMapping("/{id}")
    @PreAuthorize("@acsi.canModerateTopic(#jwtUser.id, #id)")
    public ResponseEntity<?> deleteTopic(@PathVariable UUID id,
                                         @AuthenticationPrincipal JwtUser jwtUser) {
        topicService.deleteTopic(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get all topics with pagination")
    @GetMapping
    public ResponseEntity<Page<TopicDTO>> getAllTopics(@PageableDefault(sort = "createTime",
            direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(topicService.getAllTopics(pageable).map(topicMapper::toDTO));
    }


    @Operation(summary = "Get topics by their name")
    @GetMapping("/search")
    public ResponseEntity<List<TopicDTO>> getTopicsByName(@RequestParam String name) {
        return ResponseEntity.ok(topicService.getTopicsByName(name).stream()
                .map(topicMapper::toDTO).collect(Collectors.toList()));
    }
}
