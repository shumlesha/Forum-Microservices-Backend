package com.example.files.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Entity
@Table(name = "files")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private long size;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String mimeType;

    @Column(name="owner_email", nullable = false)
    private String ownerEmail;
}
