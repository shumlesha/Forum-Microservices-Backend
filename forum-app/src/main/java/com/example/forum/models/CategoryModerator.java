package com.example.forum.models;


import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "category_moderators")
public class CategoryModerator {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

//    @ManyToOne
//    @JoinColumn(name = "moderator_id", nullable = false)
//    private User moderator;

    @Column(name = "moderator_id", nullable = false)
    private UUID moderatorId;
}
