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
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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
    ) throws BadRequestException {
        String userId = principal.getName();

        Optional<UserEntity> userEntity = this.userService.getUserById(userId);
        if (userEntity.isEmpty())
            throw new EntityNotFoundException("User not found");

        Optional<ConversationEntity> conversationEntity = this.conversationService.getConversationById(messageRequest.getConversationId());
        if (conversationEntity.isEmpty())
            throw new EntityNotFoundException("Conversation not found");

        UserEntity user = userEntity.get();
        String message = messageRequest.getMessage();
        ConversationEntity conversation = conversationEntity.get();

        List<ParticipantEntity> participantEntities = this.participantService.getParticipantListByConversation(conversation);
        List<UserEntity> participantUserList=participantEntities.stream().map(
                ParticipantEntity::getUser
        ).toList();

        MessageDTO savedMessage = this.saveMessage(message, user, conversation);
        this.webSocketService.publishToUserList(participantUserList, savedMessage);
    }

}
