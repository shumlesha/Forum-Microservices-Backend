package com.example.forum.repository;

import com.example.forum.dto.Message.MessageFilter;
import com.example.forum.models.Message;

import java.util.List;

public interface FilterMessageRepository {
    List<Message> findByFilter(MessageFilter criteria);
}
