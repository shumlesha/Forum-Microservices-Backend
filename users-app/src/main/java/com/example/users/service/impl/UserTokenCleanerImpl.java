package com.example.users.service.impl;

import com.example.common.models.User;
import com.example.common.models.VerificationToken;
import com.example.users.repository.UserRepository;
import com.example.users.repository.VerificationTokenRepository;
import com.example.users.service.TokenService;
import com.example.users.service.UserService;
import com.example.users.service.UserTokenCleaner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserTokenCleanerImpl implements UserTokenCleaner {

    private final UserService userService;
    private final TokenService tokenService;
    private static final int EXPIRATION_TIME = 30;

    @Scheduled(cron = "0 0 * * * *")
    @Override
    public void cleanExpiredTokensAndUsers() {
        List<VerificationToken> tokens = tokenService.findAll();
        Date currentTime = new Date();

        for (VerificationToken token : tokens) {
            if (isTokenExpired(token.getExpirationDate(), currentTime)) {
                User user = userService.findByEmail(token.getEmail());
                if (!user.isConfirmed()) {
                    userService.delete(user);
                    tokenService.delete(token);
                    log.info("Пользователь {} удален, так как долго не читал почту", user.getEmail());
                }
            }
        }
    }

    private boolean isTokenExpired(Date expirationDate, Date currentTime) {
        long expirationThreshold = EXPIRATION_TIME * 60 * 1000;
        return currentTime.getTime() - expirationDate.getTime() > expirationThreshold;
    }
}
