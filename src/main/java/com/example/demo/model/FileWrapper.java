package com.example.demo.model;

import lombok.Data;
import org.springframework.core.io.Resource;

@Data
public class FileWrapper {
    private String mimeType;
    private Resource file;

    private String fileName;
}
