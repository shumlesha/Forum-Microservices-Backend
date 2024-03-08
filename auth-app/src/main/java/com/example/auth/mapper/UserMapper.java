package com.example.auth.mapper;


import com.example.auth.dto.UserRegisterModel;
import com.example.auth.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserRegisterModel toDto(User user);

    User toEntity(UserRegisterModel dto);

}
