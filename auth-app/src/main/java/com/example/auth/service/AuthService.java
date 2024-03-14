package com.example.auth.service;

import com.example.securitylib.dto.TokenResponse;
import com.example.auth.dto.UserLoginModel;
import com.example.common.models.User;

public interface AuthService {
    TokenResponse register(User user);

    TokenResponse login(UserLoginModel userLoginModel);

    TokenResponse refreshToken(String refreshToken);
}
