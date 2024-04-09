package com.example.forum.service.impl;

import com.example.common.WebClientErrorResponse;
import com.example.common.dto.AttachmentDTO;
import com.example.common.dto.AttachmentsRequest;
import com.example.common.exceptions.WebClientCustomException;
import com.example.forum.models.Attachment;
import com.example.forum.models.Message;
import com.example.forum.repository.AttachmentRepository;
import com.example.forum.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {
    private final WebClient.Builder webClientBuilder;

    private final AttachmentRepository attachmentRepository;

    @Override
    @Transactional
    public List<Attachment> saveAllAttachs(Message message, List<UUID> attachments, String email) {
        List<Attachment> attachmentsConverted = new ArrayList<>();
        AttachmentsRequest attachRequest = new AttachmentsRequest();
        attachRequest.setAttachments(attachments);
        attachRequest.setEmail(email);
        List<AttachmentDTO> messageAttachments = webClientBuilder.build().post()
                .uri("http://files-app/api/internal/attachments/multipleUpload")
                .bodyValue(attachRequest)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.is4xxClientError() || httpStatus.is5xxServerError(), response -> {
                    return response.bodyToMono(WebClientErrorResponse.class).flatMap(errorBody -> {
                        return Mono.error(new WebClientCustomException(errorBody));
                    });
                })
                .bodyToFlux(AttachmentDTO.class)
                .collectList().block();

        attachmentRepository.deleteAllByMessage(message);

        for (AttachmentDTO attach: messageAttachments) {
            Attachment attachment = new Attachment();
            attachment.setSize(attach.getSize());
            attachment.setName(attach.getName());
            attachment.setFileId(attach.getFileId());
            attachment.setMessage(message);
            attachmentsConverted.add(attachmentRepository.save(attachment));
        }

        return attachmentsConverted;
    }
}
