package com.example.users.controller;


import com.example.common.models.User;
import com.example.securitylib.dto.VerificationTokenDTO;
import com.example.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping("/saveUser")
    public User createUser(@RequestBody User user) {
        log.info("Создаем юзера");
        return userService.createUser(user);
    }

    @PostMapping("/checkIfUserExists")
    public Boolean checkIfUserExists(@RequestParam String email) {
        return userService.checkIfUserExists(email);
    }

    @GetMapping("/findByEmail")
    public User findByEmail(@RequestParam String email) {
        log.info("Ищем по email");
        return userService.findByEmail(email);
    }


    @GetMapping("/findById")
    public Mono<User> findById(@RequestParam UUID id) {
        log.info("Ищем по id");
        return userService.getById(id);
    }
}
