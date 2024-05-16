package com.example.notificationapp.config.deserializers;

import com.example.common.dto.notifications.NotificationChannel;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class NotificationChannelDeserializer implements JsonDeserializer<Set<NotificationChannel>> {
    @Override
    public Set<NotificationChannel> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Set<NotificationChannel> channels = new HashSet<>();
        JsonArray jsonArray = json.getAsJsonArray();

        for (JsonElement jsonElement : jsonArray) {
            String channelString = jsonElement.getAsString();
            NotificationChannel channel = NotificationChannel.valueOf(channelString);
            channels.add(channel);
        }

        return channels;
    }
}
