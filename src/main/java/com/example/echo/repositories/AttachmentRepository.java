package com.example.echo.repositories;

import com.example.echo.entities.AttachmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AttachmentRepository extends JpaRepository<AttachmentEntity, Long> {
}
