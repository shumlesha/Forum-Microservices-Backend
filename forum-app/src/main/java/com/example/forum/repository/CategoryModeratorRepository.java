package com.example.forum.repository;

import com.example.common.models.User;
import com.example.forum.models.Category;
import com.example.forum.models.CategoryModerator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryModeratorRepository extends JpaRepository<CategoryModerator, UUID> {
    boolean existsByCategoryAndModerator(Category category, User user);

    void deleteByCategoryAndModerator(Category category, User user);

    boolean existsByModerator(User user);
}
