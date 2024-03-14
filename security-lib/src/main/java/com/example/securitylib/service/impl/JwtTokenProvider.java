package com.example.securitylib.service.impl;


import com.example.securitylib.dto.TokenResponse;
import com.example.securitylib.props.JwtProperties;
import com.example.securitylib.service.TokenProvider;
import com.example.common.enums.Role;
import com.example.common.models.User;
import com.example.common.exceptions.AccessDeniedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider implements TokenProvider {

    private final JwtProperties jwtProperties;

    private final UserDetailsService userDetailsService;

    //private final UserService userService;
    private final WebClient.Builder webClientBuilder;
    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
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
        User user = webClientBuilder.build().get()
                .uri("http://users-app/api/users/findById?id=" + userId)
                .retrieve()
                .bodyToMono(User.class)
                .block();
        tokenResponse.setId(userId);
        tokenResponse.setEmail(user.getEmail());
        tokenResponse.setAccessToken(createAccessToken(userId, user.getEmail(), user.getRoles()));
        tokenResponse.setRefreshToken(createRefreshToken(userId, user.getEmail()));
        return tokenResponse;
    }

    public boolean validateToken(String token) {
        Jws<Claims> claims = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
        return claims.getBody().getExpiration().after(new Date());
    }

    private String getId(String token) {
        return Jwts
                .parser()
                .setSigningKey(key)
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

}