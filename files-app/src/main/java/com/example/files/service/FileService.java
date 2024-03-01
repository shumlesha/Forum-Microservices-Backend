package com.example.files.service;

import com.example.files.dto.FileDTO;
import com.example.files.model.FileWrapper;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface FileService {
    UUID saveFile(MultipartFile file);
    FileWrapper downloadFile(UUID fileID);
    List<FileDTO> getFiles();
}
