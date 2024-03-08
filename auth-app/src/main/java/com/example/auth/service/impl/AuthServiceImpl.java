package com.example.auth.service.impl;


import com.example.auth.dto.TokenResponse;
import com.example.auth.dto.UserLoginModel;
import com.example.auth.enums.Role;
import com.example.auth.models.User;
import com.example.auth.repository.UserRepository;
import com.example.auth.service.AuthService;
import com.example.auth.service.TokenProvider;
import com.example.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;

    private final UserService userService;
    private final TokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public User create(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalStateException("Пользователь с email " + user.getEmail() + " уже существует");
        }
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            throw new IllegalStateException("Пароль и подтвержденный пароль не совпадают");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setConfirmPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(Role.ROLE_USER));
        return userRepository.save(user);
    }
    @Override
    public TokenResponse register(User user) {
        User createdUser = create(user);
        String accessToken = jwtTokenProvider.createAccessToken(createdUser.getId(), createdUser.getEmail(), createdUser.getRoles());
        String refreshToken = jwtTokenProvider.createRefreshToken(createdUser.getId(), createdUser.getEmail());
        return new TokenResponse(user.getId(), user.getEmail(), accessToken, refreshToken);

    }

    @Override
    public TokenResponse login(UserLoginModel userLoginModel) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginModel.getEmail(), userLoginModel.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userService.findByEmail(userLoginModel.getEmail());
        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getEmail(), user.getRoles());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId(), user.getEmail());
        return new TokenResponse(user.getId(), user.getEmail(), accessToken, refreshToken);
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        return jwtTokenProvider.refreshUserTokens(refreshToken);
    }
}
