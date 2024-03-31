package com.example.securitylib.service.impl;

import com.example.common.dto.UserDTO;
import com.example.securitylib.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Locale;


@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final MessageSource messageSource;
    private final JwtTokenProvider jwtTokenProvider;
    @Override
    public void sendEmail(UserDTO user) {
        String subject = messageSource.getMessage("email.registration.subject", null, Locale.getDefault());
        String bodyTemplate = messageSource.getMessage("email.registration.body", null, Locale.getDefault());
        String confirmationLink = "http://localhost:8989/auth/api/accounts/confirm?token="
                + jwtTokenProvider.createEmailToken(user.getId(), user.getEmail());

        String bodyMessage = bodyTemplate
                .replace("{fullName}", user.getFullName())
                .replace("{verifLink}", confirmationLink);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject(subject);
        mailMessage.setText(bodyMessage);
        mailSender.send(mailMessage);
    }
}
