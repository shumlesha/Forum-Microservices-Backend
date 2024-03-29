package com.example.securitylib.security;

import com.example.securitylib.props.ApiProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class ApiKeyFilter extends GenericFilterBean {
    private static final String API_HEADER = "API-KEY";
    private final ApiProperties apiProperties;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        String apiKey = ((HttpServletRequest) servletRequest).getHeader(API_HEADER);
        String validApiKey = apiProperties.getSecret();
        if (!validApiKey.equals(apiKey)) {
            log.info("Bad");
            ((HttpServletResponse) servletResponse).setStatus(HttpStatus.FORBIDDEN.value());
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(new IntegrationAuthentication());
        chain.doFilter(servletRequest, servletResponse);

    }
}
