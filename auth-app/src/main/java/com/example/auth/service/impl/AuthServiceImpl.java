package com.example.auth.service.impl;


import com.example.auth.dto.RegisterResponse;
import com.example.common.exceptions.AccountNotConfirmedException;
import com.example.common.exceptions.BrokenVerifyLinkException;
import com.example.common.exceptions.WebClientCustomException;
import com.example.common.models.VerificationToken;
import com.example.securitylib.dto.TokenResponse;
import com.example.auth.dto.UserLoginModel;
import com.example.common.WebClientErrorResponse;
import com.example.common.enums.Role;
import com.example.common.models.User;
import com.example.auth.service.AuthService;
import com.example.securitylib.dto.VerificationTokenDTO;
import com.example.securitylib.service.EmailService;
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

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;

    private final WebClient.Builder webClientBuilder;
    private final TokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

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
            User dbuser = webClientBuilder.build().get()
                    .uri("http://users-app/api/users/findByEmail?email=" + user.getEmail())
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.is4xxClientError() || httpStatus.is5xxServerError(), response -> {
                        return response.bodyToMono(WebClientErrorResponse.class).flatMap(errorBody -> {
                            return Mono.error(new RuntimeException(errorBody.getMessage()));
                        });
                    })
                    .bodyToMono(User.class)
                    .block();

            if (dbuser.isConfirmed()) {
                throw new IllegalStateException("Пользователь с email " + user.getEmail() + " уже существует");
            }


            return dbuser;
        }

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            throw new IllegalStateException("Пароль и подтвержденный пароль не совпадают");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setConfirmPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(Role.ROLE_USER));
        user.setConfirmed(false);


        return webClientBuilder.build().post()
                .uri("http://users-app/api/users/saveUser")
                .bodyValue(user)
                .retrieve()
                .bodyToMono(User.class).block();
        //return userRepository.save(user);
    }
    @Override
    public RegisterResponse register(User user) {
        User createdUser = create(user);

        Optional<VerificationToken> token = webClientBuilder.build().get().uri("http://users-app/api/tokens/getToken?email="+user.getEmail())
                .retrieve()
                .bodyToMono(VerificationToken.class)
                .blockOptional();

        if (token.isEmpty()) {
            emailService.sendEmail(createdUser);
        }
        else {
            if (!jwtTokenProvider.validateToken(token.get().getToken())) {
                emailService.sendEmail(createdUser);
                return new RegisterResponse(
                        createdUser.getId(),
                        createdUser.getEmail(),
                        "Вам отправлена новая ссылка для подтверждения аккаунта. Проверьте почту");
            }
        }


        log.info(String.valueOf(createdUser.getId()));
        //String accessToken = jwtTokenProvider.createAccessToken(createdUser.getId(), createdUser.getEmail(), createdUser.getRoles());
        //String refreshToken = jwtTokenProvider.createRefreshToken(createdUser.getId(), createdUser.getEmail());

        //return new TokenResponse(createdUser.getId(), createdUser.getEmail(), accessToken, refreshToken);
        return new RegisterResponse(createdUser.getId(), createdUser.getEmail(), "Проверьте почту для подтверждения аккаунта");
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

        if (!user.isConfirmed()) {
            throw new AccountNotConfirmedException("Аккаунт не подтвержден. Проверьте вашу почту");
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getEmail(), user.getRoles());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId(), user.getEmail());
        return new TokenResponse(user.getId(), user.getEmail(), accessToken, refreshToken);
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        return jwtTokenProvider.refreshUserTokens(refreshToken);
    }

    @Override
    public TokenResponse confirmUser(String token) {
        if (jwtTokenProvider.validateEmailToken(token)) {
            UUID userId = UUID.fromString(jwtTokenProvider.getId(token));
            User user = webClientBuilder.build().get()
                    .uri("http://users-app/api/users/findById?id=" + userId)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.is4xxClientError() || httpStatus.is5xxServerError(), response -> {
                        return response.bodyToMono(WebClientErrorResponse.class).flatMap(errorBody -> {
                            return Mono.error(new WebClientCustomException(errorBody));
                        });
                    })
                    .bodyToMono(User.class)
                    .block();

            if (!user.isConfirmed()) {
                user.setConfirmed(true);
                User savedUser = webClientBuilder.build().post()
                        .uri("http://users-app/api/users/saveUser")
                        .bodyValue(user)
                        .retrieve()
                        .bodyToMono(User.class).block();
                String accessToken = jwtTokenProvider.createAccessToken(savedUser.getId(), savedUser.getEmail(), savedUser.getRoles());
                String refreshToken = jwtTokenProvider.createRefreshToken(savedUser.getId(), savedUser.getEmail());
                //jwtTokenProvider.dropEmailToken(token);
                return new TokenResponse(savedUser.getId(), savedUser.getEmail(), accessToken, refreshToken);
            }
            else {
                throw new IllegalStateException("Пользователь уже подтвержден");
            }
        }
        throw new BrokenVerifyLinkException("Ссылка для подвтерждения неверна или завершился срок её действия");
    }
}
