package com.example.forum.controller;


import com.example.forum.dto.Message.CreateMessageModel;
import com.example.forum.dto.Message.EditMessageModel;
import com.example.forum.dto.Message.MessageDTO;
import com.example.forum.dto.Message.MessageFilter;
import com.example.forum.mapper.MessageMapper;
import com.example.forum.service.MessageService;
import com.example.securitylib.JwtUser;
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
public class MessageController {
    private final MessageService messageService;
    private final MessageMapper messageMapper;

    @PostMapping("/{topicId}")
    public ResponseEntity<?> createMessage(@PathVariable UUID topicId,
                                           @Validated @RequestBody CreateMessageModel createMessageModel,
                                           @AuthenticationPrincipal JwtUser jwtUser) {
        messageService.createMessage(topicId, jwtUser.getId(), createMessageModel);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editMessage(@PathVariable UUID id,
                                         @Validated @RequestBody EditMessageModel editMessageModel,
                                         @AuthenticationPrincipal JwtUser jwtUser) {
        messageService.editMessage(id,jwtUser.getEmail(), editMessageModel);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> deleteMessage(@PathVariable UUID id) {
        messageService.deleteMessage(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{topicId}")
    public ResponseEntity<Page<MessageDTO>> getMessages(@PathVariable UUID topicId,
                                                     @PageableDefault(sort = "createTime",
                                                             direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(messageService.getMessages(topicId, pageable).map(messageMapper::toDTO));
    }


    @GetMapping("/search")
    public ResponseEntity<List<MessageDTO>> getMessagesByContent(@RequestParam String content) {
        return ResponseEntity.ok(messageService.getMessagesByContent(content)
                .stream().map(messageMapper::toDTO).collect(Collectors.toList()));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<MessageDTO>> searchMessages(@ModelAttribute MessageFilter messageFilter) {
        return ResponseEntity.ok(messageService.searchMessages(messageFilter)
                .stream().map(messageMapper::toDTO).collect(Collectors.toList()));
    }
}
