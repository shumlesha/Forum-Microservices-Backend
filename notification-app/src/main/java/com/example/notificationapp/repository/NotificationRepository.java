package com.example.notificationapp.repository;

import com.example.notificationapp.models.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    Page<Notification> findByReceiverEmail(String email, Pageable pageable);

    @Query("select notif from Notification notif where notif.receiverEmail = ?1 and (upper(notif.topic) like concat('%', upper(?2), '%') or upper(notif.content) like concat('%',upper(?2),'%'))")
    Page<Notification> findByReceiverEmailAndTopicContainingIgnoreCaseOrContentContainingIgnoreCase(String email, String queryText, String queryText1, Pageable pageable);

    Page<Notification> findByReceiverEmailOrderByIsReadAsc(String email, Pageable pageable);

    @Modifying
    @Query("update Notification notif set notif.isRead = true where notif.id in ?1")
    void readNotifications(Set<UUID> notificationIds);

    long countByReceiverEmailAndIsReadFalse(String email);
}
