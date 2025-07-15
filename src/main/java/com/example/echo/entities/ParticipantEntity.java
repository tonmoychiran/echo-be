package com.example.echo.entities;


import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "participant")
public class ParticipantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String participantId;


    @ManyToOne
    @JoinColumn(name = "conversation_id", nullable = false)
    private ConversationEntity conversation;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column
    private Long joinedAt;

    public ParticipantEntity(ConversationEntity conversation, UserEntity user) {
        this.conversation = conversation;
        this.user = user;
    }

    public ParticipantEntity() {
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public ConversationEntity getConversation() {
        return conversation;
    }

    public void setConversation(ConversationEntity conversation) {
        this.conversation = conversation;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Long getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Long joinedAt) {
        this.joinedAt = joinedAt;
    }

    @PrePersist
    protected void onCreate() {
        this.joinedAt = Instant.now().toEpochMilli();
    }
}
