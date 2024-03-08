package com.example.auth.service.impl;

import com.example.auth.models.User;
import com.example.auth.repository.UserRepository;
import com.example.auth.service.UserService;

import com.example.common.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User findByEmail(String email) {

        return userRepository.getByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Пользователь с таким email не найден"));
    }

    @Override
    public User getById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с таким id не найден: " + userId));
    }
}
