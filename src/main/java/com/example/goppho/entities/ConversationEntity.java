package com.example.goppho.entities;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "conversation")
public class ConversationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String conversationId;

    @Column(length = 200, nullable = true)
    private String conversationName;

    @Column
    private Boolean isGroup;

    @Column
    private Long createdAt;

    @OneToMany(mappedBy = "conversation")
    private List<ParticipantEntity> participants;

    public ConversationEntity(String conversationName) {
        this.conversationName = conversationName;
        this.isGroup = true;
    }

    public ConversationEntity() {
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getConversationName() {
        return conversationName;
    }

    public void setConversationName(String conversationName) {
        this.conversationName = conversationName;
    }

    public Boolean getGroup() {
        return isGroup;
    }

    public void setGroup(Boolean group) {
        isGroup = group;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public List<ParticipantEntity> getParticipants() {
        return participants;
    }

    public void setParticipants(List<ParticipantEntity> participants) {
        this.participants = participants;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now().toEpochMilli();
    }
}
