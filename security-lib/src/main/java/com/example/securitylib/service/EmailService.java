package com.example.securitylib.service;

import com.example.common.models.User;

import java.util.Properties;

public interface EmailService {
    void sendEmail(User user);
}
