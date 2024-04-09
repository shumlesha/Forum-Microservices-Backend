package com.example.securitylib.security;

import com.example.common.exceptions.ResourceNotFoundException;
import com.example.securitylib.service.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@AllArgsConstructor
@Slf4j
public class JwtTokenFilter extends GenericFilterBean {
    private final TokenProvider jwtTokenProvider;


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String bearerToken = ((HttpServletRequest) servletRequest).getHeader("Authorization");
        log.info("Запрос дошел");
        log.info(servletRequest.getRemoteAddr());
        log.info(((HttpServletRequest) servletRequest).getHeader("X-Forwarded-For"));
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken = bearerToken.substring(7);
        }
        if (bearerToken != null && jwtTokenProvider.validateEmailToken(bearerToken)) {
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpResponse.setContentType("application/json");
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.getWriter().write("{ \"message\": \"Не пытайтесь авторизоваться по email-токену\" }");
            return;
        }
        if (bearerToken != null && jwtTokenProvider.validateToken(bearerToken)) {
            try {
                Authentication authentication = jwtTokenProvider.getAuthentication(bearerToken);
                log.info(authentication.getName());
                if (authentication != null) {


                    if (!jwtTokenProvider.checkUserConfirmation(bearerToken)) {
                        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
                        httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        httpResponse.setContentType("application/json");
                        httpResponse.setCharacterEncoding("UTF-8");
                        httpResponse.getWriter().write("{ \"message\": \"Подтвердите свой аккаунт через email\" }");
                        return;
                    }

                    if (jwtTokenProvider.isBanned(bearerToken)) {
                        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
                        httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        httpResponse.setContentType("application/json");
                        httpResponse.setCharacterEncoding("UTF-8");
                        httpResponse.getWriter().write("{ \"message\": \"Вы забанены. Обратитесь к администратору форума\" }");
                        return;
                    }

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            catch (ResourceNotFoundException ignored) {

            }
        }
        filterChain.doFilter(servletRequest, servletResponse);

    }
}

