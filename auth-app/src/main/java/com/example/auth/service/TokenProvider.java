package com.example.auth.service;

import com.example.auth.dto.TokenResponse;
import com.example.auth.enums.Role;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@Component
public interface TokenProvider {
    String createAccessToken(UUID userId, String email, Set<Role> roles);
    String createRefreshToken(UUID userId, String email);
    TokenResponse refreshUserTokens(String refreshToken);
    boolean validateToken(String token);
    Authentication getAuthentication(String token);
}
