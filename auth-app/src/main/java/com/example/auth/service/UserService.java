package com.example.auth.service;


import com.example.auth.models.User;

import java.util.UUID;

public interface UserService {
    User findByEmail(String email);

    User getById(UUID userId);
}
