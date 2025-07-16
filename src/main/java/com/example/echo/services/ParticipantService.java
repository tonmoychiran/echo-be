package com.example.echo.services;

import com.example.echo.entities.ConversationEntity;
import com.example.echo.entities.ParticipantEntity;
import com.example.echo.entities.UserEntity;
import com.example.echo.repositories.ParticipantRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParticipantService {
    ParticipantRepository participantRepository;

    @Autowired
    public ParticipantService(
            ParticipantRepository participantRepository
    ) {
        this.participantRepository = participantRepository;
    }

    @Transactional
    protected List<ParticipantEntity> createNewParticipants(
            List<UserEntity> users,
            ConversationEntity conversation
    ) {
        List<ParticipantEntity> participantEntities = users.stream().map(user ->
                new ParticipantEntity(conversation, user)
        ).collect(Collectors.toList());

        return this.participantRepository.saveAll(participantEntities);
    }

    @Transactional
    protected Optional<ParticipantEntity> getParticipantByUserAndConversation(
            UserEntity user,
            ConversationEntity conversation
    ) {
        return this.participantRepository.findByUserAndConversation(user, conversation);
    }

    @Transactional
    protected List<ParticipantEntity> getParticipantsByConversation(
            ConversationEntity conversation
    ) {
        return this.participantRepository.findAllByConversation(conversation);
    }
}
