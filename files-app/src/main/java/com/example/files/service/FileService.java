package com.example.files.service;

import com.example.common.dto.AttachmentDTO;
import com.example.common.dto.AttachmentsRequest;
import com.example.files.dto.FileDTO;
import com.example.files.model.File;
import com.example.files.model.FileWrapper;
import com.querydsl.core.Fetchable;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface FileService {
    UUID saveFile(MultipartFile file, String email);
    FileWrapper downloadFile(UUID fileID);
    List<File> getFiles();

    List<AttachmentDTO> multipleUpload(AttachmentsRequest attachRequest);

    List<File> getPersonalFiles(String email);
}
