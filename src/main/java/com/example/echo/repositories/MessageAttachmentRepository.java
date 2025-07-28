package com.example.echo.repositories;

import com.example.echo.entities.MessageAttachmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageAttachmentRepository extends JpaRepository<MessageAttachmentEntity, Long> {
}
