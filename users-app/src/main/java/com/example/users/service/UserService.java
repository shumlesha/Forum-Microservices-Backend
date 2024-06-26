package com.example.users.service;



import com.example.common.dto.UserDTO;
import com.example.users.models.User;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserService {
    User findByEmail(String email);

    User getById(UUID userId);

    User createUser(User user);

    Boolean checkIfUserExists(String email);


    void delete(User user);
    
    User registerNewUser(User user);

    void editUser(User user, UUID userId);

    void banUser(UUID userId, UUID currentUserId);

    void unbanUser(UUID userId, UUID currentUserId);

    Boolean checkIfUserBanned(String email);
}
