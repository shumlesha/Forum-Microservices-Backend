package com.example.users.service.impl;

import com.example.common.dto.UserDTO;
import com.example.common.enums.Role;
import com.example.common.exceptions.ObjectAlreadyExistsException;
import com.example.users.models.User;
import com.example.users.repository.UserRepository;

import com.example.common.exceptions.ResourceNotFoundException;
import com.example.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public User findByEmail(String email) {

        return userRepository.getByEmailIgnoreCase(email).orElseThrow(
                () -> new ResourceNotFoundException("Пользователь с таким email не найден"));
    }

    @Override
    public User getById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("Пользователь с таким id не найден: " + userId));
    }

    @Override
    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Boolean checkIfUserExists(String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    @Override
    @Transactional
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public User registerNewUser(User user) {
        if (userRepository.existsByEmailIgnoreCase(user.getEmail())) {
            throw new ObjectAlreadyExistsException("Пользователь с email " + user.getEmail() + " не существует");
        }

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            throw new IllegalStateException("Пароль и подтвержденный пароль не совпадают");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setConfirmPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(Role.ROLE_USER));
        user.setConfirmed(true);

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void editUser(User user, UUID userId) {
        User userEntity = userRepository.findById(userId)
                .orElseThrow(() ->  new ResourceNotFoundException("Пользователя с таким id не существует"));


        if (!user.getPassword().equals(user.getConfirmPassword())) {
            throw new IllegalStateException("Пароль и подтвержденный пароль не совпадают");
        }

        userEntity.setFullName(user.getFullName());
        userEntity.setBirthDate(user.getBirthDate());
        userEntity.setPhoneNumber(user.getPhoneNumber());
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userEntity.setConfirmPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(userEntity);
    }

    @Override
    @Transactional
    public void banUser(UUID userId, UUID currentUserId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->  new ResourceNotFoundException("Пользователя с таким id не существует " + userId));

        if (user.isBanned()) {
            throw new IllegalStateException("Пользователь уже забанен. Возможно, вы хотели его разбанить?");
        }


        if (userId.equals(currentUserId)) {
            throw new IllegalStateException("Вы не можете забанить самого себя");
        }

        user.setBanned(true);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void unbanUser(UUID userId, UUID currentUserId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->  new ResourceNotFoundException("Пользователя с таким id не существует " + userId));

        if (!user.isBanned()) {
            throw new IllegalStateException("Пользователь не забанен. Возможно, вы хотели его забанить?");
        }

        if (userId.equals(currentUserId)) {
            throw new IllegalStateException("Вы не можете разбанить самого себя");
        }

        user.setBanned(false);
        userRepository.save(user);
    }

    @Override
    public Boolean checkIfUserBanned(String email) {
        User user = findByEmail(email);

        return user.isBanned();
    }


}
