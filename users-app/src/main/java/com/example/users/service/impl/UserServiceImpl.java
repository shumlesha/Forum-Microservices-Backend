package com.example.users.service.impl;

import com.example.common.models.User;
import com.example.users.repository.UserRepository;

import com.example.common.exceptions.ResourceNotFoundException;
import com.example.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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
    public Mono<User> getById(UUID userId) {
        return Mono.justOrEmpty(userRepository.findById(userId))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Пользователь с таким id не найден: " + userId)));
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Boolean checkIfUserExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }


}
