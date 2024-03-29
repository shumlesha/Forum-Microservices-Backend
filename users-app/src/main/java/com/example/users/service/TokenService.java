package com.example.users.service;

import com.example.securitylib.dto.VerificationTokenDTO;
import com.example.common.models.VerificationToken;

import java.util.List;
import java.util.Optional;

public interface TokenService {
    void createToken(VerificationTokenDTO verificationTokenDTO);

    Boolean checkIfTokenExists(String token);

    void deleteToken(String token);

    Optional<VerificationToken> getToken(String email);

    List<VerificationToken> findAll();

    void delete(VerificationToken token);
}
