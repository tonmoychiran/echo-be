package com.example.echo.repositories;

import com.example.echo.entities.ConversationEntity;
import com.example.echo.entities.ParticipantEntity;
import com.example.echo.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<ParticipantEntity, String> {
    Optional<ParticipantEntity> findByUserAndConversation(UserEntity user, ConversationEntity conversation);
    List<ParticipantEntity> findAllByConversation(ConversationEntity conversation);
}
