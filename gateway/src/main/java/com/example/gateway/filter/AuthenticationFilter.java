package com.example.gateway.filter;

import com.example.common.exceptions.AccessDeniedException;
import com.example.gateway.util.TokenValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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
