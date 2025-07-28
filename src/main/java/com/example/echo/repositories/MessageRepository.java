package com.example.echo.repositories;


import com.example.echo.entities.ConversationEntity;
import com.example.echo.entities.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    List<MessageEntity> findFirst50ByConversationAndMessageIdLessThanOrderByMessageIdDesc(ConversationEntity conversation, Long messageId);

    List<MessageEntity> findFirst50ByConversationOrderByMessageIdDesc(ConversationEntity conversation);

}
