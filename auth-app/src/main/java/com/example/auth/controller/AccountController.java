package com.example.auth.controller;


import com.example.securitylib.dto.TokenResponse;
import com.example.auth.service.AuthService;
import com.example.auth.dto.UserLoginModel;
import com.example.auth.dto.UserRegisterModel;
import com.example.auth.dto.validation.OnCreate;
import com.example.auth.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final UserMapper userMapper;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> registerUser(@Validated(OnCreate.class) @RequestBody UserRegisterModel userRegisterModel) {
        TokenResponse tokenResponse = authService.register(userMapper.toEntity(userRegisterModel));
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> loginUser(@Validated @RequestBody UserLoginModel userLoginModel) {

        return ResponseEntity.ok(authService.login(userLoginModel));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody String refreshToken) {
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }

}
