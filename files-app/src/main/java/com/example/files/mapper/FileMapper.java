package com.example.files.mapper;

import com.example.files.dto.FileDTO;
import com.example.files.model.File;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileMapper {
    FileDTO toDto(File file);
}
