package com.example.echo.repositories;

import com.example.echo.entities.ConversationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<ConversationEntity, String> {
    @Query(value = """
            SELECT c.* FROM conversation c
            JOIN participant p ON c.conversation_id = p.conversation_id
            WHERE c.is_group = false 
            AND p.user_id IN (:userIds)
            GROUP BY c.conversation_id
            HAVING COUNT(DISTINCT p.user_id) = 2""",
            nativeQuery = true)
    Optional<ConversationEntity> findPrivateConversation(@Param("userIds") List<String> userIds);
}
