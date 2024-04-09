package com.example.files.service.impl;

import com.example.common.dto.AttachmentDTO;
import com.example.common.dto.AttachmentsRequest;
import com.example.common.exceptions.AccessDeniedException;
import com.example.common.exceptions.NotFileOwnerException;
import com.example.files.FileRepository;
import com.example.files.dto.FileDTO;
import com.example.common.exceptions.FileNotFoundException;
import com.example.common.exceptions.FileUploadException;
import com.example.files.model.File;
import com.example.files.model.FileWrapper;
import com.example.files.service.FileService;
import com.example.files.service.props.MinioProperties;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @Transactional
    public UUID saveFile(MultipartFile file, String email) {
        if (file.isEmpty() || file.getOriginalFilename() == null) {
            throw new FileUploadException(HttpStatus.BAD_REQUEST, "Неверный формат файла");
        }

        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

        File fileObj = new File();
        fileObj.setName(file.getOriginalFilename());
        fileObj.setSize(file.getSize());
        fileObj.setDateTime(LocalDateTime.now());
        fileObj.setContent(fileName);
        fileObj.setMimeType(file.getContentType());
        fileObj.setOwnerEmail(email);
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
            throw new FileUploadException(HttpStatus.INTERNAL_SERVER_ERROR, "Не удалось скачать файл");
        }
    }

    @Override
    public List<File> getFiles() {
        return fileRepository.findAll();
    }

    @Override
    public List<AttachmentDTO> multipleUpload(AttachmentsRequest attachRequest) {
        List<AttachmentDTO> attachedFiles = new ArrayList<>();

        String email = attachRequest.getEmail();
        for (UUID fileId: attachRequest.getAttachments()) {
            File fileObj = fileRepository.findById(fileId)
                   .orElseThrow(() -> new FileNotFoundException(HttpStatus.NOT_FOUND, "Не найдено файла с таким id:  " + fileId));

            if (!email.equals(fileObj.getOwnerEmail())) {
                throw new NotFileOwnerException("Вы не являетесь владельцем одного из прикрепленных файлов");
            }

            AttachmentDTO fileDTO = new AttachmentDTO();
            fileDTO.setName(fileObj.getName());
            fileDTO.setSize(fileObj.getSize());
            fileDTO.setFileId(fileObj.getId());

            attachedFiles.add(fileDTO);

        }
        return attachedFiles;
    }
}
