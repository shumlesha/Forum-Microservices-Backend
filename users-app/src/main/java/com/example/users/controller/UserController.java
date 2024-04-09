package com.example.users.controller;


import com.example.common.dto.UserCreateModel;
import com.example.common.dto.UserDTO;
import com.example.common.dto.UserEditModel;
import com.example.common.dto.validation.OnCreate;
import com.example.securitylib.JwtUser;
import com.example.users.mapper.UserMapper;
import com.example.users.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("api/users")
@RequiredArgsConstructor
@Tag(name = "Users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/create")
    public ResponseEntity<UserDTO> createUser(@Validated(OnCreate.class) @RequestBody UserCreateModel userCreateModel) {

        return ResponseEntity.ok(userMapper.toDto(userService.registerNewUser(userMapper.toEntityFromAdminDTO(userCreateModel))));
    }

    @PutMapping("/edit/{userId}")
    public ResponseEntity<?> editUser(@Validated @RequestBody UserEditModel userEditModel,
                                      @PathVariable UUID userId) {
        userService.editUser(userMapper.toEntityFromAdminEditDTO(userEditModel), userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/ban/{userId}")
    public ResponseEntity<?> banUser(@PathVariable UUID userId, @AuthenticationPrincipal JwtUser jwtUser) {
        userService.banUser(userId, jwtUser.getId());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/unban/{userId}")
    public ResponseEntity<?> unbanUser(@PathVariable UUID userId, @AuthenticationPrincipal JwtUser jwtUser) {
        userService.unbanUser(userId, jwtUser.getId());
        return ResponseEntity.ok().build();
    }
}
