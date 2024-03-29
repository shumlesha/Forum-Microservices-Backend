package com.example.users.service.impl;

import com.example.securitylib.dto.VerificationTokenDTO;
import com.example.common.models.VerificationToken;
import com.example.users.repository.VerificationTokenRepository;
import com.example.users.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final VerificationTokenRepository verificationTokenRepository;
    @Override
    @Transactional
    public void createToken(VerificationTokenDTO verificationTokenDTO) {
        if (verificationTokenRepository.existsByEmail(verificationTokenDTO.getEmail())) {
            verificationTokenRepository.deleteAllByEmail(verificationTokenDTO.getEmail());
        }
        VerificationToken token = new VerificationToken();
        token.setToken(verificationTokenDTO.getToken());
        token.setExpirationDate(verificationTokenDTO.getExpirationDate());
        token.setEmail(verificationTokenDTO.getEmail());
        verificationTokenRepository.save(token);
    }

    @Override
    public Boolean checkIfTokenExists(String token) {
        return verificationTokenRepository.existsByToken(token);
    }

    @Override
    @Transactional
    public void deleteToken(String token) {
        VerificationToken dbtoken = verificationTokenRepository.findByToken(token);
        verificationTokenRepository.delete(dbtoken);
    }

    @Override
    public Optional<VerificationToken> getToken(String email) {
        return verificationTokenRepository.findByEmail(email);
    }

    @Override
    public List<VerificationToken> findAll() {
        return verificationTokenRepository.findAll();
    }

    @Override
    public void delete(VerificationToken token) {
        verificationTokenRepository.delete(token);
    }
}
