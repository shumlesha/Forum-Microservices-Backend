package com.example.files.controller;


import com.example.files.dto.FileDTO;
import com.example.files.mapper.FileMapper;
import com.example.files.model.FileWrapper;
import com.example.files.service.FileService;
import com.example.securitylib.JwtUser;
import io.minio.credentials.Jwt;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/files")
@Tag(name = "Files")
public class FileController {

    private final FileService fileService;
    private final FileMapper fileMapper;

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Save file in S3-source")
    public ResponseEntity<UUID> saveFile(@RequestParam("file") MultipartFile file,
                                         @AuthenticationPrincipal JwtUser jwtUser) {
        log.info("Загружаем файл {}", file.getName());
        return ResponseEntity.ok(fileService.saveFile(file, jwtUser.getEmail()));
    }

    @GetMapping("/download/{fileId}")
    @Operation(summary = "Download file from S3-source")
    public ResponseEntity<Resource> downloadFile(@PathVariable UUID fileId) {
        log.info("Скачиваем файл с id {}", fileId);
        FileWrapper file = fileService.downloadFile(fileId);
        log.info(file.getMimeType());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getMimeType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(file.getFile());
    }

    @GetMapping("/getAll")
    @Operation(summary = "Get all files from S3-source")
    public ResponseEntity<List<FileDTO>> getFiles() {
        log.info("Получаем список файлов");
        return ResponseEntity.ok(fileService.getFiles().stream()
                .map(fileMapper::toDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/getMy")
    @Operation(summary = "Get all personal uploaded files from S3-source")
    public ResponseEntity<List<FileDTO>> getPersonalFiles(@AuthenticationPrincipal JwtUser jwtUser) {
        log.info("Только свои файлы");
        return ResponseEntity.ok(fileService.getPersonalFiles(jwtUser.getEmail()).stream()
                .map(fileMapper::toDto)
                .collect(Collectors.toList())
        );
    }
}
