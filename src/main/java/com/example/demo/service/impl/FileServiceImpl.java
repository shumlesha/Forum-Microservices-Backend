package com.example.demo.service.impl;

import com.example.demo.FileRepository;
import com.example.demo.dto.FileDTO;
import com.example.demo.exceptions.FileNotFoundException;
import com.example.demo.exceptions.FileUploadException;
import com.example.demo.model.File;
import com.example.demo.model.FileWrapper;
import com.example.demo.service.FileService;
import com.example.demo.service.props.MinioProperties;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileServiceImpl implements FileService {


    private final FileRepository fileRepository;
    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    @Value("${minio.bucketName}")
    private String bucketName;
    @Override
    public UUID saveFile(MultipartFile file) {
        if (file.isEmpty() || file.getOriginalFilename() == null) {
            throw new FileUploadException(HttpStatus.BAD_REQUEST, "Неверный формат файла");
        }

        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

        File fileObj = File.builder()
                .name(file.getOriginalFilename())
                .size(file.getSize())
                .dateTime(LocalDateTime.now())
                .content(fileName)
                .mimeType(file.getContentType())
                .build();
        fileObj = fileRepository.save(fileObj);

        putFile(fileName, file);

        return fileObj.getId();
    }

    @SneakyThrows
    private void putFile(String fileName, MultipartFile file) {
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );
    }

    @Override
    public FileWrapper downloadFile(UUID fileId) {
        File fileObj = fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException(HttpStatus.NOT_FOUND, "Не найдено файла с таким id:  " + fileId));
        try {
            InputStream inputStream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileObj.getContent())
                            .build()
            );
            log.info("Хотим скачать файл {}", fileObj.getContent());
            FileWrapper fileInfo = new FileWrapper();
            fileInfo.setFile(new InputStreamResource(inputStream, fileObj.getName()));
            fileInfo.setMimeType(fileObj.getMimeType());
            fileInfo.setFileName(fileObj.getName());
            return fileInfo;
        } catch (Exception e) {
            throw new FileUploadException(HttpStatus.BAD_REQUEST, "Не удалось скачать файл");
        }
    }

    @Override
    public List<FileDTO> getFiles() {
        List<File> allFiles = fileRepository.findAll();
        return allFiles.stream()
                .map(FileDTO::from)
                .collect(Collectors.toList());
    }
}
