package com.example.echo.services;

import com.example.echo.entities.ConversationEntity;
import com.example.echo.entities.MessageEntity;
import com.example.echo.entities.ParticipantEntity;
import com.example.echo.entities.UserEntity;
import com.example.echo.repositories.MessageRepository;
import com.example.echo.requests.MessageRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MessageService {

    MessageRepository messageRepository;
    UserService userService;
    ConversationService conversationService;
    ParticipantService participantService;

    @Autowired
    public MessageService(
            MessageRepository messageRepository,
            UserService userService,
            ConversationService conversationService,
            ParticipantService participantService
    ) {
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.conversationService = conversationService;
        this.participantService = participantService;
    }

    @Transactional
    public MessageEntity createNewMessage(
            MessageRequest messageRequest,
            String conversationId,
            String userId
    ) {
        Optional<UserEntity> userEntity = this.userService.getUserById(userId);
        if (userEntity.isEmpty())
            throw new EntityNotFoundException("User not found");

        Optional<ConversationEntity> conversationEntity = this.conversationService.getConversationById(conversationId);
        if (conversationEntity.isEmpty())
            throw new EntityNotFoundException("Conversation not found");

        UserEntity user = userEntity.get();
        ConversationEntity conversation = conversationEntity.get();

        Optional<ParticipantEntity> participantEntity = this.participantService.getParticipantByUserAndConversation(user, conversation);
        if (participantEntity.isEmpty())
            throw new EntityNotFoundException("User not allowed");

        MessageEntity message = new MessageEntity(
                messageRequest.getMessage(),
                user,
                conversation
        );

        return messageRepository.save(message);
    }
}
