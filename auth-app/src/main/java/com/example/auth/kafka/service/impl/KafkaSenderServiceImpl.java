package com.example.auth.kafka.service.impl;

import com.example.auth.kafka.service.KafkaSenderService;
import com.example.common.dto.notifications.NotificationDTO;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;


@Service
@RequiredArgsConstructor
public class KafkaSenderServiceImpl implements KafkaSenderService {
    private final KafkaSender<String, NotificationDTO> sender;

    @Override
    public void send(NotificationDTO notificationDTO) {
//        Gson gson = new Gson();
//        String json = gson.toJson(notificationDTO);

        sender.send(
                Mono.just(SenderRecord.create(
                        "notifications",
                        0,
                        System.currentTimeMillis(),
                        String.valueOf(notificationDTO.hashCode()),
                        notificationDTO,
                        null)
        )).subscribe();
    }
}
