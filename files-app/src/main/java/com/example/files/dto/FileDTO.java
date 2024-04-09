package com.example.files.dto;

import com.example.files.model.File;
import jakarta.annotation.sql.DataSourceDefinitions;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class FileDTO {
    private UUID id;
    private String name;
    private long size;
    private LocalDateTime dateTime;
}
