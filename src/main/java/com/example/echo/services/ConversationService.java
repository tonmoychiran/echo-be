package com.example.echo.services;

import com.example.echo.entities.ConversationEntity;
import com.example.echo.entities.ParticipantEntity;
import com.example.echo.entities.UserEntity;
import com.example.echo.repositories.ConversationRepository;
import com.example.echo.requests.CreateNewConversationRequest;
import com.example.echo.requests.CreateNewGroupConversationRequest;
import com.example.echo.responses.GetResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
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
    public GetResponse<ConversationEntity> getPrivateConversation(
            UserDetails userDetails,
            String friendUserId
    ) {
        UserEntity user = this.userService.getUserFromUserDetails(userDetails);
        Optional<UserEntity> friendUser = this.userService.getUserById(friendUserId);
        if (friendUser.isEmpty())
            throw new EntityNotFoundException("Friend not found");

        UserEntity friend = friendUser.get();
        List<String> userIdList = List.of(friend.getUserId(), user.getUserId());

        Optional<ConversationEntity> privateConversation = this.conversationRepository.findPrivateConversation(userIdList);
        if (privateConversation.isPresent()) {
            return new GetResponse<>("Conversation", privateConversation.get());
        }

        ConversationEntity conversationEntity = this.createNewPrivateConversation(
                List.of(user, friend)
        );
        return new GetResponse<>("Conversation", conversationEntity);
    }

    @Transactional
    protected ConversationEntity createNewPrivateConversation(
            List<UserEntity> userList
    ) {
        ConversationEntity newConversation = new ConversationEntity();
        ConversationEntity conversationEntity = this.conversationRepository.save(newConversation);
        this.participantService.createNewParticipants(userList, conversationEntity);
        return conversationEntity;
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
