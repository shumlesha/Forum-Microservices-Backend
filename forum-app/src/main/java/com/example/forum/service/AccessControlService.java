package com.example.forum.service;

import java.util.UUID;

public interface AccessControlService {

    boolean canModerateCategory(UUID userId, UUID parentId);

    boolean canModerateTopic(UUID userId, UUID topicId);

    boolean isTopicOwner(UUID userId, UUID topicId);

    boolean isMessageOwner(UUID userId, UUID messageId);

    boolean canModerateMessage(UUID userId, UUID messageId);
}
