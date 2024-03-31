package com.example.auth.service;

import com.example.auth.dto.RegisterResponse;
import com.example.common.dto.UserDTO;
import com.example.securitylib.dto.TokenResponse;
import com.example.auth.dto.UserLoginModel;


public interface AuthService {
    RegisterResponse register(UserDTO user);

    TokenResponse login(UserLoginModel userLoginModel);

    TokenResponse refreshToken(String refreshToken);

    TokenResponse confirmUser(String token);
}
