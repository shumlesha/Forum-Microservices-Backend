package com.example.forum.controller;


import com.example.forum.dto.Message.CreateMessageModel;
import com.example.forum.dto.Message.EditMessageModel;
import com.example.forum.dto.Message.MessageDTO;
import com.example.forum.dto.Message.MessageFilter;
import com.example.forum.mapper.MessageMapper;
import com.example.forum.models.Message;
import com.example.forum.service.MessageService;
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
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Tag(name = "Message")
public class MessageController {
    private final MessageService messageService;
    private final MessageMapper messageMapper;

    @Operation(summary = "Create message in concrete topic")
    @PostMapping("/{topicId}")
    public ResponseEntity<MessageDTO> createMessage(@PathVariable UUID topicId,
                                           @Validated @RequestBody CreateMessageModel createMessageModel,
                                           @AuthenticationPrincipal JwtUser jwtUser) {
        Message createdMessage = messageService.createMessage(topicId, jwtUser.getId(), createMessageModel);
        return ResponseEntity.ok(messageMapper.toDTO(createdMessage));
    }

    @Operation(summary = "Edit message by its id")
    @PutMapping("/{id}")
    public ResponseEntity<?> editMessage(@PathVariable UUID id,
                                         @Validated @RequestBody EditMessageModel editMessageModel,
                                         @AuthenticationPrincipal JwtUser jwtUser) {
        messageService.editMessage(id,jwtUser.getEmail(), editMessageModel);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete message by its id")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable UUID id) {
        messageService.deleteMessage(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get messages with pagination by topicId")
    @GetMapping("/{topicId}")
    public ResponseEntity<Page<MessageDTO>> getMessages(@PathVariable UUID topicId,
                                                     @PageableDefault(sort = "createTime",
                                                             direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(messageService.getMessages(topicId, pageable).map(messageMapper::toDTO));
    }

    @Operation(summary = "Get messages by content-text")
    @GetMapping("/search")
    public ResponseEntity<List<MessageDTO>> getMessagesByContent(@RequestParam String content) {
        return ResponseEntity.ok(messageService.getMessagesByContent(content)
                .stream().map(messageMapper::toDTO).collect(Collectors.toList()));
    }

    @Operation(summary = "Get messages by some filter-params")
    @GetMapping("/filter")
    public ResponseEntity<List<MessageDTO>> searchMessages(@ModelAttribute MessageFilter messageFilter) {
        return ResponseEntity.ok(messageService.searchMessages(messageFilter)
                .stream().map(messageMapper::toDTO).collect(Collectors.toList()));
    }
}
