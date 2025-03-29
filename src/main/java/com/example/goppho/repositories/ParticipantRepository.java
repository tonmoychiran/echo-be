package com.example.goppho.repositories;

import com.example.goppho.entities.ConversationEntity;
import com.example.goppho.entities.ParticipantEntity;
import com.example.goppho.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<ParticipantEntity, String> {
    @Query("SELECT p FROM ParticipantEntity p WHERE p.user = :user AND p.conversation = :conversationId")
    Optional<ParticipantEntity> findByUserAndConversation(UserEntity user, ConversationEntity conversation);
}
