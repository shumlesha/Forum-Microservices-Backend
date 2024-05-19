package com.example.files.repository;

import com.example.files.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<File, UUID> {
    List<File> findAllByIdIn(List<UUID> attachments);

    List<File> findAllByOwnerEmail(String email);
}
