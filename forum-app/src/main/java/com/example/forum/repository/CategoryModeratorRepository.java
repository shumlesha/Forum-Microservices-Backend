package com.example.forum.repository;


import com.example.forum.models.Category;
import com.example.forum.models.CategoryModerator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryModeratorRepository extends JpaRepository<CategoryModerator, UUID> {
    boolean existsByCategoryAndModeratorId(Category category, UUID moderatorId);

    void deleteByCategoryAndModeratorId(Category category, UUID moderatorId);

    boolean existsByModeratorId(UUID moderatorId);
}
