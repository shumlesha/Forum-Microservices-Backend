package com.example.notificationapp.controller;


import com.example.notificationapp.dto.NotificationUserDTO;
import com.example.notificationapp.dto.NotificationsCountDTO;
import com.example.notificationapp.mapper.NotificationMapper;
import com.example.notificationapp.models.Notification;
import com.example.notificationapp.service.NotificationService;
import com.example.securitylib.JwtUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification")
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;

    @Operation(summary = "Get own notifications, using pagination, querytext, with sort (by notification creation time)")
    @PutMapping
    public ResponseEntity<Page<NotificationUserDTO>> getNotifications(@AuthenticationPrincipal JwtUser jwtUser,
                                                                      @RequestParam(required = false) String queryText,
                                                                      @ParameterObject @PageableDefault(sort = "createTime",
                                                                              direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(notificationService.getNotifications(jwtUser, queryText, pageable).map(notificationMapper::toDto));
    }


    @Operation(summary = "Get count of own non-read notifications")
    @GetMapping("/nonread-count")
    public ResponseEntity<NotificationsCountDTO> getNonReadNotificationsCount(@AuthenticationPrincipal JwtUser jwtUser) {
        return ResponseEntity.ok(new NotificationsCountDTO(notificationService.getNonReadNotificationsCount(jwtUser)));
    }

}
