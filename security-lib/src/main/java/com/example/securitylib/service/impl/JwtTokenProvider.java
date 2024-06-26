package com.example.securitylib.service.impl;


import com.example.common.WebClientErrorResponse;
import com.example.common.dto.UserDTO;
import com.example.common.dto.VerificationTokenDTO;
import com.example.common.exceptions.BrokenVerifyLinkException;
import com.example.common.exceptions.WebClientCustomException;
import com.example.securitylib.dto.TokenResponse;
import com.example.securitylib.props.JwtProperties;
import com.example.securitylib.service.TokenProvider;
import com.example.common.enums.Role;
import com.example.common.exceptions.AccessDeniedException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider implements TokenProvider {

    private final JwtProperties jwtProperties;

    private final UserDetailsService userDetailsService;

    //private final UserService userService;
    private final WebClient.Builder webClientBuilder;
    private Key key;
    private Key emailKey;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
        this.emailKey = Keys.hmacShaKeyFor(jwtProperties.getEmailSecret().getBytes());
    }

    public String createAccessToken(UUID userId, String email, Set<Role> roles) {

        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.getAccess());

        return Jwts.builder()
                .setSubject(email)
                .claim("id", userId.toString())
                .claim("roles", mapRoles(roles))
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key)
                .compact();
    }

    private List<String> mapRoles(Set<Role> roles) {
        return roles.stream()
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    public String createRefreshToken(UUID userId, String email) {

        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.getRefresh());
        return Jwts.builder()
                .claim("id", userId.toString())
                .claim("email", email)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key)
                .compact();
    }

    public TokenResponse refreshUserTokens(String refreshToken) {
        TokenResponse tokenResponse = new TokenResponse();

        if (!validateToken(refreshToken)) {
            throw new AccessDeniedException();
        }

        UUID userId = UUID.fromString(getId(refreshToken));
        //User user = userService.getById(userId);
        UserDTO user = webClientBuilder.build().get()
                .uri("http://users-app/api/internal/users/findById?id=" + userId)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block();
        tokenResponse.setId(userId);
        tokenResponse.setEmail(user.getEmail());
        tokenResponse.setAccessToken(createAccessToken(userId, user.getEmail(), user.getRoles()));
        tokenResponse.setRefreshToken(createRefreshToken(userId, user.getEmail()));
        return tokenResponse;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return claims.getBody().getExpiration().after(new Date());
        }
        catch (ExpiredJwtException e) {
            return false;
        }
    }

    public String getId(String token) {
        return Jwts
                .parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("id")
                .toString();
    }

    public String getIdFromEmailToken(String token) {
        return Jwts
                .parser()
                .setSigningKey(emailKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("id")
                .toString();
    }

    private String getEmail(String token) {
        return Jwts
                .parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    public Authentication getAuthentication(String token) {
        String email = getEmail(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    @Override
    public String createEmailToken(UUID userId, String email) {

        Date now = new Date();
        Date validity = new Date(now.getTime() + 600000);

        String createdToken = Jwts.builder()
                .claim("id", userId.toString())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(emailKey)
                .compact();


        VerificationTokenDTO token = new VerificationTokenDTO();
        token.setToken(createdToken);
        token.setExpirationDate(validity);
        token.setEmail(email);
        webClientBuilder.build().post()
                .uri("http://users-app/api/internal/tokens/createToken")
                .bodyValue(token)
                .exchange()
                .block();


        return createdToken;
    }

    @Override
    public boolean checkUserConfirmation(String token) {
        UUID userId = UUID.fromString(getId(token));
        UserDTO user = webClientBuilder.build().get()
                .uri("http://users-app/api/internal/users/findById?id=" + userId)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.is4xxClientError() || httpStatus.is5xxServerError(), response -> {
                    return response.bodyToMono(WebClientErrorResponse.class).flatMap(errorBody -> {
                        return Mono.error(new WebClientCustomException(errorBody));
                    });
                })
                .bodyToMono(UserDTO.class)
                .block();
        return user.isConfirmed();
    }

    @Override
    public boolean validateEmailToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(emailKey)
                    .build()
                    .parseClaimsJws(token);

            boolean isTokenInDb = Boolean.TRUE.equals(webClientBuilder.build().post()
                    .uri("http://users-app/api/internal/tokens/checkIfTokenExists?token=" + token)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block());

            //boolean typeIsEmail = (claims.getBody().get("type", String.class) != null);

            return claims.getBody().getExpiration().after(new Date()) && (isTokenInDb);
        }
        catch (ExpiredJwtException e) {
            return false;
        }
        catch (MalformedJwtException e) {
            throw new BrokenVerifyLinkException("Ссылка для подтверждения неверна");
        }

    }


    @Override
    public void dropEmailToken(String token) {

        webClientBuilder.build().delete()
                .uri("http://users-app/api/internal/tokens/deleteToken?token=" + token)
                .exchange()
                .block();

    }

    @Override
    public boolean isBanned(String token) {
        return Boolean.TRUE.equals(webClientBuilder.build().get()
                .uri("http://users-app/api/internal/users/checkIfUserBanned?email=" + getEmail(token))
                .retrieve()
                .onStatus(httpStatus -> httpStatus.is4xxClientError() || httpStatus.is5xxServerError(), response -> {
                    return response.bodyToMono(WebClientErrorResponse.class).flatMap(errorBody -> {
                        return Mono.error(new WebClientCustomException(errorBody));
                    });
                })
                .bodyToMono(Boolean.class)
                .block());
    }

}