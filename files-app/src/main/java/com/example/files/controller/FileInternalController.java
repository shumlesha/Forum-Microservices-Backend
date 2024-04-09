package com.example.files.controller;


import com.example.common.dto.AttachmentDTO;
import com.example.common.dto.AttachmentsRequest;
import com.example.files.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/internal/attachments")
public class FileInternalController {

    private final FileService fileService;


    @PostMapping("/multipleUpload")
    @Operation(summary = "Upload multiple files (get attachments) by ids")
    public List<AttachmentDTO> multipleUpload(@RequestBody AttachmentsRequest attachRequest) {
        return fileService.multipleUpload(attachRequest);
    }
}
