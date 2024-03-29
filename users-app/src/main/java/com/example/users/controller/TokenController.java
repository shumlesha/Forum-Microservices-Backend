package com.example.users.controller;

import com.example.securitylib.dto.VerificationTokenDTO;
import com.example.common.models.VerificationToken;
import com.example.users.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Slf4j
@RestController
@RequestMapping("/api/tokens")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("/createToken")
    public void createToken(@RequestBody VerificationTokenDTO verificationTokenDTO) {
        tokenService.createToken(verificationTokenDTO);

    }

    @PostMapping("/checkIfTokenExists")
    public Boolean checkIfTokenExists(@RequestParam String token) {
        return tokenService.checkIfTokenExists(token);
    }

    @DeleteMapping("/deleteToken")
    public void deleteToken(@RequestParam String token) {
        tokenService.deleteToken(token);
    }

    @GetMapping("/getToken")
    public Optional<VerificationToken> getToken(@RequestParam String email) {
        return tokenService.getToken(email);
    }
}
