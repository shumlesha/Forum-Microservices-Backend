package com.example.gateway.filter;


import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> openApiEndpoints = List.of(
            "/auth/api/accounts/login",
            "/auth/api/accounts/register",
            "/eureka",
            "/swagger-ui.html",
            "/v3/api-docs"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints.stream().noneMatch(uri -> request.getURI().getPath().contains(uri));

}
