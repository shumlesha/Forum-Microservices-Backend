package com.example.forum.controller;


import com.example.forum.dto.Admin.AppointModeratorModel;
import com.example.forum.dto.Admin.RemoveModeratorModel;
import com.example.forum.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin")
public class AdminController {
    private final AdminService adminService;

    @Operation(summary = "Appoint a moderator to concrete category")
    @PostMapping("/{userId}")
    public ResponseEntity<?> appointModerator(@PathVariable UUID userId,
                                              @Validated @RequestBody AppointModeratorModel appointModeratorModel) {
        adminService.appointModerator(userId, appointModeratorModel);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remove moderator from concrete category")
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> removeModerator(@PathVariable UUID userId,
                                             @Validated @RequestBody RemoveModeratorModel removeModeratorModel) {
        adminService.removeModerator(userId, removeModeratorModel);

        return ResponseEntity.ok().build();
    }
}
