package com.example.notificationapp.service.impl.channel_impls.EmailChannel;

import com.example.common.dto.UserDTO;
import com.example.common.dto.notifications.NotificationDTO;
import com.example.notificationapp.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;


    @Override
    public void sendPersonalEmail(String email, NotificationDTO notificationDTO) {
        String subject = notificationDTO.getTopic();
        String bodyMessage = notificationDTO.getContent();

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(bodyMessage);
        mailSender.send(mailMessage);
    }
}
