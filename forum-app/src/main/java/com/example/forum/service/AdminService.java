package com.example.forum.service;

import com.example.forum.dto.Admin.AppointModeratorModel;
import com.example.forum.dto.Admin.RemoveModeratorModel;

import java.util.UUID;

public interface AdminService {

    void appointModerator(UUID userId, AppointModeratorModel appointModeratorModel);

    void removeModerator(UUID userId, RemoveModeratorModel removeModeratorModel);
}
