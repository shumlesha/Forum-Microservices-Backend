package com.example.securitylib.service;


import com.example.common.enums.Role;

import com.example.securitylib.dto.TokenResponse;
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
    String createEmailToken(UUID userId, String email);

    String getId(String token);

    boolean checkUserConfirmation(String token);

    boolean validateEmailToken(String token);

    void dropEmailToken(String token);
}
