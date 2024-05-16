package com.example.forum.kafka.service.impl;



import com.example.common.dto.notifications.NotificationDTO;
import com.example.forum.kafka.service.KafkaSenderService;
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
