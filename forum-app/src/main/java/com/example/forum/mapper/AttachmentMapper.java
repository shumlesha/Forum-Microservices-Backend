package com.example.forum.mapper;

import com.example.common.dto.AttachmentDTO;
import com.example.forum.models.Attachment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AttachmentMapper {

    AttachmentDTO toDto(Attachment attachment);
}
