package com.example.users.service;



import com.example.common.models.User;
import com.example.securitylib.dto.VerificationTokenDTO;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User findByEmail(String email);

    Mono<User> getById(UUID userId);

    User createUser(User user);

    Boolean checkIfUserExists(String email);


    void delete(User user);
}
