package com.example.demo.dto;

import com.example.demo.model.File;
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

    public static FileDTO from(File file) {
        FileDTO dto = new FileDTO();
        dto.setId(file.getId());
        dto.setName(file.getName());
        dto.setSize(file.getSize());
        dto.setDateTime(file.getDateTime());
        return dto;
    }
}
