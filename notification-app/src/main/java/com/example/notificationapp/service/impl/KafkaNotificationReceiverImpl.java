package com.example.notificationapp.service.impl;

import com.example.common.dto.notifications.NotificationChannel;
import com.example.common.dto.notifications.NotificationDTO;

import com.example.notificationapp.config.deserializers.LocalDateDeserializer;
import com.example.notificationapp.config.deserializers.LocalDateTimeDeserializer;
import com.example.notificationapp.config.deserializers.NotificationChannelDeserializer;
import com.example.notificationapp.service.KafkaNotificationReceiver;
import com.example.notificationapp.service.KafkaNotificationService;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.kafka.receiver.KafkaReceiver;

import java.io.StringReader;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaNotificationReceiverImpl implements KafkaNotificationReceiver {

    private final Map<String, KafkaNotificationService> handlers;
    private final KafkaReceiver<String, NotificationDTO> receiver;
    private final LocalDateTimeDeserializer localDateTimeDeserializer;
    private final LocalDateDeserializer localDateDeserializer;
    private final KafkaNotificationService kafkaNotificationService;

    @PostConstruct
    private void init() {
        fetch();
    }

    @Override
    public void fetch() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class,
                        localDateTimeDeserializer)
                .registerTypeAdapter(LocalDate.class, localDateDeserializer)
                .registerTypeAdapter(new TypeToken<Set<NotificationChannel>>() {}.getType(), new NotificationChannelDeserializer())
                .setLenient()
                .create();
        receiver.receive().subscribe(r -> {
            try {
                //System.out.println("Получаем значение: " + r.value().toString());
                NotificationDTO notificationDTO = gson.fromJson(String.valueOf(r.value()), NotificationDTO.class);
                //kafkaNotificationService.handle(notificationDTO, gson);
                System.out.println("Удачный парсинг: " + notificationDTO.getTopic());

                if (notificationDTO.getChannels().size() == 1 && notificationDTO.getChannels().contains(NotificationChannel.ALL)) {
                    for (String channel: handlers.keySet()) {
                        handlers.get(channel).handle(notificationDTO, gson);
                    }
                }
                else{
                    for (NotificationChannel channel: notificationDTO.getChannels()) {
                        handlers.get(String.valueOf(channel)).handle(notificationDTO, gson);
                    }
                }

                r.receiverOffset().acknowledge();
            } catch (Exception e) {
                System.err.println("Gson десериализовал с ошибкой: " + e.getMessage());
                System.out.println(Arrays.toString(e.getStackTrace()));
                //System.out.println(r.value().toString());
            }
        });
    }
}
