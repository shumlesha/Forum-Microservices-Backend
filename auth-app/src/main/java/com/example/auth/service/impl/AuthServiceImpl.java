package com.example.auth.service.impl;


import com.example.securitylib.dto.TokenResponse;
import com.example.auth.dto.UserLoginModel;
import com.example.common.WebClientErrorResponse;
import com.example.common.enums.Role;
import com.example.common.models.User;
import com.example.auth.service.AuthService;
import com.example.securitylib.service.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;

    private final WebClient.Builder webClientBuilder;
    private final TokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public User create(User user) {

        /*if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalStateException("Пользователь с email " + user.getEmail() + " уже существует");
        }*/

        boolean isUserExists = Boolean.TRUE.equals(webClientBuilder.build().post()
                .uri("http://users-app/api/users/checkIfUserExists?email=" + user.getEmail())
                .retrieve()
                .bodyToMono(Boolean.class)
                .block());

        if (isUserExists) {
            throw new IllegalStateException("Пользователь с email " + user.getEmail() + " уже существует");
        }

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            throw new IllegalStateException("Пароль и подтвержденный пароль не совпадают");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setConfirmPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(Role.ROLE_USER));

        return webClientBuilder.build().post()
                .uri("http://users-app/api/users/saveUser")
                .bodyValue(user)
                .retrieve()
                .bodyToMono(User.class).block();
        //return userRepository.save(user);
    }
    @Override
    public TokenResponse register(User user) {
        User createdUser = create(user);
        log.info(String.valueOf(createdUser.getId()));
        String accessToken = jwtTokenProvider.createAccessToken(createdUser.getId(), createdUser.getEmail(), createdUser.getRoles());
        String refreshToken = jwtTokenProvider.createRefreshToken(createdUser.getId(), createdUser.getEmail());
        return new TokenResponse(createdUser.getId(), createdUser.getEmail(), accessToken, refreshToken);

    }

    @Override
    public TokenResponse login(UserLoginModel userLoginModel) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginModel.getEmail(), userLoginModel.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //User user = userService.findByEmail(userLoginModel.getEmail());
        User user = webClientBuilder.build().get()
                .uri("http://users-app/api/users/findByEmail?email=" + userLoginModel.getEmail())
                .retrieve()
                .onStatus(httpStatus -> httpStatus.is4xxClientError() || httpStatus.is5xxServerError(), response -> {
                    return response.bodyToMono(WebClientErrorResponse.class).flatMap(errorBody -> {
                        return Mono.error(new RuntimeException(errorBody.getMessage()));
                    });
                })
                .bodyToMono(User.class)
                .block();
        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getEmail(), user.getRoles());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId(), user.getEmail());
        return new TokenResponse(user.getId(), user.getEmail(), accessToken, refreshToken);
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        return jwtTokenProvider.refreshUserTokens(refreshToken);
    }
}
