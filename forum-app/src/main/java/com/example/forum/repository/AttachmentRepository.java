package com.example.forum.repository;

import com.example.forum.models.Attachment;
import com.example.forum.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, UUID> {
    void deleteAllByMessage(Message message);
}
