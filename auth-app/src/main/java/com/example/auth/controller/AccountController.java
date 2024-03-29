package com.example.auth.controller;


import com.example.auth.dto.RegisterResponse;
import com.example.securitylib.dto.TokenResponse;
import com.example.auth.service.AuthService;
import com.example.auth.dto.UserLoginModel;
import com.example.auth.dto.UserRegisterModel;
import com.example.auth.dto.validation.OnCreate;
import com.example.auth.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Tag(name = "Account")
public class AccountController {

    private final UserMapper userMapper;
    private final AuthService authService;

    @Operation(summary = "Register new user-account")
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@Validated(OnCreate.class) @RequestBody UserRegisterModel userRegisterModel) {
        RegisterResponse registerResponse = authService.register(userMapper.toEntity(userRegisterModel));
        return ResponseEntity.ok(registerResponse);
    }

    @Operation(summary = "Login into your account")
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> loginUser(@Validated @RequestBody UserLoginModel userLoginModel) {

        return ResponseEntity.ok(authService.login(userLoginModel));
    }

    @Operation(summary = "Refresh token")
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody String refreshToken) {
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }

    @Hidden
    @GetMapping("/confirm")
    public ResponseEntity<TokenResponse> confirmUser(@RequestParam String token) {
        return ResponseEntity.ok(authService.confirmUser(token));
    }

}
