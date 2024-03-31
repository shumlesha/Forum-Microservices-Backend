package com.example.users.controller;


import com.example.common.dto.UserDTO;
import com.example.users.mapper.UserMapper;
import com.example.users.models.User;
import com.example.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/saveUser")
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        log.info("Создаем юзера");
        return userMapper.toDto(userService.createUser(userMapper.toEntity(userDTO)));
    }

    @PostMapping("/checkIfUserExists")
    public Boolean checkIfUserExists(@RequestParam String email) {
        return userService.checkIfUserExists(email);
    }

    @GetMapping("/findByEmail")
    public UserDTO findByEmail(@RequestParam String email) {
        log.info("Ищем по email");
        return userMapper.toDto(userService.findByEmail(email));
    }


    @GetMapping("/findById")
    public UserDTO findById(@RequestParam UUID id) {
        log.info("Ищем по id");
        return userMapper.toDto(userService.getById(id));
    }
}
