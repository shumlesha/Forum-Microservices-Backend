package com.example.forum.models;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "attachments")
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    public String name;

    @Column(nullable = false)
    public long size;

    @Column(name="file_id", nullable = false)
    private UUID fileId;

    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;

}
