package com.example.echo.services;

import com.example.echo.dtos.MessageDTO;
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

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    UserService userService;
    WebSocketService webSocketService;
    MessageRepository messageRepository;
    ParticipantService participantService;
    ConversationService conversationService;

    @Autowired
    public MessageService(
            UserService userService,
            WebSocketService webSocketService,
            MessageRepository messageRepository,
            ParticipantService participantService,
            ConversationService conversationService
    ) {
        this.userService = userService;
        this.webSocketService = webSocketService;
        this.messageRepository = messageRepository;
        this.participantService = participantService;
        this.conversationService = conversationService;
    }

    @Transactional
    protected MessageDTO saveMessage(
            String message,
            UserEntity user,
            ConversationEntity conversation
    ) {
        MessageEntity newMessage = new MessageEntity(
                message,
                user,
                conversation
        );

        MessageEntity savedMessage = this.messageRepository.save(newMessage);
        return new MessageDTO(
                savedMessage.getMessageId(),
                savedMessage.getMessage(),
                savedMessage.getCreatedAt(),
                savedMessage.getUser(),
                savedMessage.getConversation().getConversationId()
        );
    }

    @Transactional
    public void createNewDMMessage(
            MessageRequest messageRequest,
            Principal principal
    ) {
        String userId = principal.getName();

        Optional<UserEntity> userEntity = this.userService.getUserById(userId);
        if (userEntity.isEmpty())
            throw new EntityNotFoundException("User not found");

        Optional<ConversationEntity> conversationEntity = this.conversationService.getConversationById(messageRequest.getConversationId());
        if (conversationEntity.isEmpty())
            throw new EntityNotFoundException("Conversation not found");
        ConversationEntity conversation = conversationEntity.get();

        List<ParticipantEntity> participantEntities=this.participantService.getParticipantsByConversation(conversation);

        UserEntity user = userEntity.get();
        String message = messageRequest.getMessage();




        MessageDTO savedMessage = this.saveMessage(message, user, conversation);


    }

}
