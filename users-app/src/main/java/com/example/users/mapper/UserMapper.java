package com.example.users.mapper;



import com.example.common.dto.UserCreateModel;
import com.example.common.dto.UserDTO;
import com.example.common.dto.UserEditModel;
import com.example.users.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDto(User user);

    User toEntity(UserDTO dto);

    User toEntityFromAdminDTO(UserCreateModel userCreateModel);

    User toEntityFromAdminEditDTO(UserEditModel userEditModel);
}
