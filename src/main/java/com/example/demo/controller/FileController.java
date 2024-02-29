package com.example.demo.controller;


import com.example.demo.dto.FileDTO;
import com.example.demo.model.FileWrapper;
import com.example.demo.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {

    private FileService fileService;

    @PostMapping("/save")
    public ResponseEntity<UUID> saveFile(@RequestParam("file") MultipartFile file) {
        log.info("Загружаем файл {}", file.getName());
        return ResponseEntity.ok(fileService.saveFile(file));
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable UUID fileId) {
        log.info("Скачиваем файл с id {}", fileId);
        FileWrapper file = fileService.downloadFile(fileId);
        log.info(file.getMimeType());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(file.getFile());
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<FileDTO>> getFiles() {
        log.info("Получаем список файлов");
        return ResponseEntity.ok(fileService.getFiles());
    }
}
