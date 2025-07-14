package com.example.goppho.repositories;

import com.example.goppho.entities.ConversationEntity;
import com.example.goppho.entities.ParticipantEntity;
import com.example.goppho.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<ParticipantEntity, String> {
    Optional<ParticipantEntity> findByUserAndConversation(UserEntity user, ConversationEntity conversation);
}
