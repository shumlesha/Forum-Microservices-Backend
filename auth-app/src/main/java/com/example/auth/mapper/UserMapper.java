package com.example.auth.mapper;


import com.example.auth.dto.UserRegisterModel;
import com.example.common.dto.UserDTO;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserRegisterModel toDto(UserDTO user);

    UserDTO toEntity(UserRegisterModel dto);

}
