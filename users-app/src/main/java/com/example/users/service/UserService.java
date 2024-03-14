package com.example.users.service;



import com.example.common.models.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User findByEmail(String email);

    User getById(UUID userId);

    User createUser(User user);

    Boolean checkIfUserExists(String email);
}
