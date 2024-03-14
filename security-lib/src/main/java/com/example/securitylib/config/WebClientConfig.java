package com.example.securitylib.config;


import com.example.securitylib.props.ApiProperties;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private final ApiProperties apiProperties;

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {

        return WebClient.builder()
                .defaultHeader("API-KEY", apiProperties.getSecret());
    }
}
