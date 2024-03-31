package com.example.users.repository;

import com.example.users.models.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, UUID> {
    boolean existsByToken(String token);

    VerificationToken findByToken(String token);

    boolean existsByEmail(String email);

    void deleteAllByEmail(String email);

    Optional<VerificationToken> findByEmail(String email);
}
