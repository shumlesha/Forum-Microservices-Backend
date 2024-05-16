package com.example.notificationapp.service.impl.channel_impls.EmailChannel;


import com.example.common.dto.UserDTO;
import com.example.common.dto.notifications.NotificationDTO;
import com.example.common.dto.notifications.NotificationType;
import com.example.notificationapp.service.KafkaNotificationService;
import com.example.notificationapp.service.EmailService;
import com.example.notificationapp.service.NotificationService;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Service("EMAIL")
@RequiredArgsConstructor
public class EmailNotificationServiceImpl implements KafkaNotificationService {
    private final EmailService emailService;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public void handle(NotificationDTO notificationDTO, Gson gson) {
        //System.out.println("Check: " + notificationDTO.toString());

        boolean toSave = notificationDTO.isSaveInHistory();

        if (notificationDTO.getNotificationType().equals(NotificationType.PERSONAL)) {
            emailService.sendPersonalEmail(notificationDTO.getReceiverEmail(), notificationDTO);

            if (toSave) {
                notificationService.save(notificationDTO.getReceiverEmail(), notificationDTO);
            }
        }
        else {
            Type listObject = new TypeToken<ArrayList<String>>() {}.getType();
            List<String> emails = gson.fromJson(gson.toJson(notificationDTO.getPayload()), listObject);

            System.out.println(emails);
            if (!toSave) {
                for (String email : emails) {
                    emailService.sendPersonalEmail(email, notificationDTO);
                }
            }
            else{
                for (String email : emails) {
                    emailService.sendPersonalEmail(email, notificationDTO);
                    notificationService.save(email, notificationDTO);
                }
            }

        }
    }

}
