package com.example.forum.controller;

import com.example.forum.dto.Message.CreateMessageModel;
import com.example.forum.dto.Message.EditMessageModel;
import com.example.forum.models.Category;
import com.example.forum.models.Message;
import com.example.forum.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.support.PageableUtils;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @PostMapping("/{topicId}")
    public ResponseEntity<?> createMessage(@PathVariable UUID topicId,
                                           @Validated @RequestBody CreateMessageModel createMessageModel) {
        messageService.createMessage(topicId, createMessageModel);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editMessage(@PathVariable UUID id,
                                           @Validated @RequestBody EditMessageModel editMessageModel) {
        messageService.editMessage(id, editMessageModel);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable UUID id) {
        messageService.deleteMessage(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{topicId}")
    public ResponseEntity<Page<Message>> getMessages(@PathVariable UUID topicId,
                                                     @PageableDefault(sort = "createTime",
                                                             direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(messageService.getMessages(topicId, pageable));
    }


    @GetMapping("/search")
    public ResponseEntity<List<Message>> getMessagesByContent(@RequestParam String content) {
        return ResponseEntity.ok(messageService.getMessagesByContent(content));
    }
}
