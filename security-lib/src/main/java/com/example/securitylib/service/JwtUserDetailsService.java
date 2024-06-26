package com.example.securitylib.service;

import com.example.common.WebClientErrorResponse;
import com.example.common.dto.UserDTO;
import com.example.common.exceptions.WebClientCustomException;
import com.example.securitylib.security.JwtUserFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final WebClient.Builder webClientBuilder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //User user = userService.findByEmail(email);
        UserDTO user = webClientBuilder.build().get()
                .uri("http://users-app/api/internal/users/findByEmail?email=" + email)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.is4xxClientError() || httpStatus.is5xxServerError(), response -> {
                    return response.bodyToMono(WebClientErrorResponse.class).flatMap(errorBody -> {
                        return Mono.error(new WebClientCustomException(errorBody));
                    });
                })
                .bodyToMono(UserDTO.class)
                .block();
        return JwtUserFactory.create(user);
    }


}