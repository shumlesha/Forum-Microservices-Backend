package com.example.gateway.filter;

import com.example.common.exceptions.AccessDeniedException;
import com.example.gateway.util.TokenValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.reactive.CorsUtils;
import reactor.core.publisher.Mono;

@Component

public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator routeValidator;

    @Autowired
    private TokenValidator tokenValidator;

    public AuthenticationFilter() {
        super(Config.class);
    }


    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {

            if (routeValidator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey("Authorization")) {
                    throw new org.springframework.security.access.AccessDeniedException("Authorization header is missing");
                }

                String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                try {
                    tokenValidator.validateToken(authHeader);

                    exchange.getRequest()
                            .mutate()
                            .header("Authorization", "Bearer " + authHeader);
                }
                catch (Exception e) {
                    throw new AccessDeniedException();
                }

            }


           return chain.filter(exchange);
        });
    }

    public static class Config {

    }
}
