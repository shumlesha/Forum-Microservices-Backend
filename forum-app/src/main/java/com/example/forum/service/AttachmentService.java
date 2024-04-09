package com.example.forum.service;

import com.example.forum.models.Attachment;
import com.example.forum.models.Message;

import java.util.List;
import java.util.UUID;

public interface AttachmentService {
    List<Attachment> saveAllAttachs(Message message, List<UUID> attachments, String email);
}
