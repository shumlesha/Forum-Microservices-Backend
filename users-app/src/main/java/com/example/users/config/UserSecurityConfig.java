package com.example.users.config;

import com.example.securitylib.props.ApiProperties;
import com.example.securitylib.security.ApiKeyFilter;
import com.example.securitylib.security.JwtTokenFilter;
import com.example.securitylib.service.TokenProvider;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class UserSecurityConfig {


    private final ApplicationContext applicationContext;
    private final ApiProperties apiProperties;
    private final TokenProvider tokenProvider;



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain ApiFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .securityMatcher("/api/internal/**")
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(configurer ->
                        configurer.authenticationEntryPoint(((request, response, authException) -> {
                                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                                    response.getWriter().write("Unauthorized");
                                }))
                                .accessDeniedHandler(((request, response, accessDeniedException) -> {
                                    response.setStatus(HttpStatus.FORBIDDEN.value());
                                    response.getWriter().write("Unauthorized");
                                }))
                )
                .authorizeHttpRequests(configurer ->
                        configurer
                                .anyRequest().authenticated()
                )
                .anonymous(AbstractHttpConfigurer::disable)
                .addFilterBefore(new ApiKeyFilter(apiProperties), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }


    @Bean
    public SecurityFilterChain JwtFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(configurer ->
                        configurer.authenticationEntryPoint(((request, response, authException) -> {
                                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                                    response.getWriter().write("Unauthorized");
                                }))
                                .accessDeniedHandler(((request, response, accessDeniedException) -> {
                                    response.setStatus(HttpStatus.FORBIDDEN.value());
                                    response.getWriter().write("Unauthorized");
                                }))
                )
                .authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers("/swagger-ui/**",
                                        "/swagger-ui.*",
                                        "/v3/api-docs",
                                        "/v3/api-docs/**",
                                        "/swagger-ui.html",
                                        "/swagger-resources/**",
                                        "/configuration/ui",
                                        "/configuration/security",
                                        "/swagger-ui.html",
                                        "/webjars/**",
                                        "/v3/**",
                                        "/users/v3/api-docs",
                                        "/swagger-ui/**",
                                        "/v3/api-docs.*",
                                        "/swagger*/**").permitAll()
                                .anyRequest().authenticated()
                )
                .anonymous(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtTokenFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }




//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity
//                .csrf(AbstractHttpConfigurer::disable)
//                .httpBasic(AbstractHttpConfigurer::disable)
//                .sessionManagement(sessionManagement ->
//                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                )
//                .exceptionHandling(configurer ->
//                        configurer.authenticationEntryPoint(((request, response, authException) -> {
//                                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
//                                    response.getWriter().write("Unauthorized");
//                                }))
//                                .accessDeniedHandler(((request, response, accessDeniedException) -> {
//                                    response.setStatus(HttpStatus.FORBIDDEN.value());
//                                    response.getWriter().write("Unauthorized");
//                                }))
//                )
//                .authorizeHttpRequests(configurer ->
//                        configurer
//                                .requestMatchers("/swagger-ui/**",
//                                        "/swagger-ui.*",
//                                        "/v3/api-docs",
//                                        "/v3/api-docs/**",
//                                        "/swagger-ui.html",
//                                        "/swagger-resources/**",
//                                        "/configuration/ui",
//                                        "/configuration/security",
//                                        "/swagger-ui.html",
//                                        "/webjars/**",
//                                        "/v3/**",
//                                        "/users/v3/api-docs",
//                                        "/swagger-ui**/**",
//                                        "/v3/api-docs.*",
//                                        "/swagger*/**").permitAll()
//                        //.requestMatchers("/api/internal/**").authenticated()
//                                .anyRequest().authenticated()
//                )
//                .anonymous(AbstractHttpConfigurer::disable)
//                .addFilterBefore(new JwtTokenFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(new ApiKeyFilter(apiProperties), UsernamePasswordAuthenticationFilter.class);
//        return httpSecurity.build();
//    }


    @Bean
    public OpenAPI customOpenAPI(@Value("${gateway.host}") String gatewayHost) {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth").addList("apiKeyAuth"))
                .components(
                        new Components()
                                .addSecuritySchemes("bearerAuth", new SecurityScheme().type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer").bearerFormat("JWT"))
                                .addSecuritySchemes("apiKeyAuth", new SecurityScheme().type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER).name("API-KEY"))
                )
                .addServersItem(new Server().url(gatewayHost + "/users"));
    }
}