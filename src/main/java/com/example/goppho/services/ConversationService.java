package com.example.goppho.services;

import com.example.goppho.entities.ConversationEntity;
import com.example.goppho.entities.ParticipantEntity;
import com.example.goppho.entities.UserEntity;
import com.example.goppho.repositories.ConversationRepository;
import com.example.goppho.repositories.ParticipantRepository;
import com.example.goppho.repositories.UserRepository;
import com.example.goppho.requests.CreateNewConversationRequest;
import com.example.goppho.requests.CreateNewGroupConversationRequest;
import com.example.goppho.responses.CreateNewConversationResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
