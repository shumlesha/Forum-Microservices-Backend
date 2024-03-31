package com.example.users.mapper;

import com.example.common.dto.VerificationTokenDTO;
import com.example.users.models.VerificationToken;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface VerificationTokenMapper {
    VerificationToken toEntity(VerificationTokenDTO verificationTokenDTO);

    VerificationTokenDTO toDto(VerificationToken verificationToken);

}