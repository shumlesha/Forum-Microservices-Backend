package com.example.forum.controller;


import com.example.forum.dto.Topic.FavoriteTopicDTO;
import com.example.forum.mapper.FavoriteTopicMapper;
import com.example.forum.service.FavoriteTopicService;
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

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
@Tag(name = "Favorite")
public class FavoriteTopicController {
    private final FavoriteTopicService favoriteTopicService;
    private final FavoriteTopicMapper favoriteTopicMapper;

    @Operation(summary = "Add topic to favorites")
    @PostMapping("/{topicId}")
    public ResponseEntity<?> addFavoriteTopic(@PathVariable UUID topicId,
            @AuthenticationPrincipal JwtUser jwtUser){
        favoriteTopicService.addFavoriteTopic(topicId, jwtUser);

        return ResponseEntity.ok().build();
    }


    @Operation(summary = "Delete topic from favorites")
    @DeleteMapping("/{topicId}")
    public ResponseEntity<?> deleteFavoriteTopic(@PathVariable UUID topicId,
            @AuthenticationPrincipal JwtUser jwtUser){
        favoriteTopicService.deleteFavoriteTopic(topicId, jwtUser);

        return ResponseEntity.ok().build();
    }


    @Operation(summary = "Get your own topics from favorites")
    @GetMapping
    public ResponseEntity<Page<FavoriteTopicDTO>> getFavoriteTopics(@AuthenticationPrincipal JwtUser jwtUser,
                                                                    @ParameterObject @PageableDefault(sort = "createTime",
                                                direction = Sort.Direction.DESC) Pageable pageable){
        return ResponseEntity.ok(favoriteTopicService.getFavoriteTopics(jwtUser, pageable).map(favoriteTopicMapper::toDTO));
    }
}
