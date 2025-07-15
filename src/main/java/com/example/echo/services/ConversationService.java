package com.example.echo.services;

import com.example.echo.entities.ConversationEntity;
import com.example.echo.entities.UserEntity;
import com.example.echo.repositories.ConversationRepository;
import com.example.echo.requests.CreateNewConversationRequest;
import com.example.echo.requests.CreateNewGroupConversationRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConversationService {

    ConversationRepository conversationRepository;
    ParticipantService participantService;
    UserService userService;

    @Autowired
    public ConversationService(
            ConversationRepository conversationRepository,
            ParticipantService participantService,
            UserService userService
    ) {
        this.conversationRepository = conversationRepository;
        this.participantService = participantService;
        this.userService = userService;
    }

    @Transactional
    protected Optional<ConversationEntity> isPrivateConversationExists(
            List<String> users
    ) {
        return this.conversationRepository.findPrivateConversation(users);

    }

    @Transactional
    protected ConversationEntity createNewPrivateConversation(
            CreateNewConversationRequest newConversationRequest
    ) {
        List<String> users = newConversationRequest.getUsers();
        ConversationEntity newConversationEntity = this.conversationRepository.save(
                new ConversationEntity()
        );
        List<UserEntity> userEntities = this.userService.getUserListById(users);
        this.participantService.createNewParticipants(userEntities, newConversationEntity);
        return newConversationEntity;
    }

    @Transactional
    protected ConversationEntity createNewGroupConversation(
            CreateNewGroupConversationRequest newConversationRequest
    ) {
        newConversationRequest.isUsersListContainsOnlyTwoUser();
        List<String> users = newConversationRequest.getUsers();
        ConversationEntity newConversationEntity = this.conversationRepository.save(new ConversationEntity(
                newConversationRequest.getConversationName()
        ));
        List<UserEntity> userEntities = this.userService.getUserListById(users);
        this.participantService.createNewParticipants(userEntities, newConversationEntity);
        return newConversationEntity;
    }

    @Transactional
    protected Optional<ConversationEntity> getConversationById(String conversationId) {
        return this.conversationRepository.findById(conversationId);
    }
}
