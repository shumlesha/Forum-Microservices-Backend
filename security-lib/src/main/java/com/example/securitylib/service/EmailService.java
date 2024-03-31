package com.example.securitylib.service;

import com.example.common.dto.UserDTO;


public interface EmailService {
    void sendEmail(UserDTO user);
}
