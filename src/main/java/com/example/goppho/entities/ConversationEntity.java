package com.example.goppho.entities;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "conversation")
public class ConversationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String conversationId;

    @Column(length = 200, nullable = true)
    private String conversationName;

    @Column
    private Long createdAt;

    @OneToMany(mappedBy = "conversation")
    private List<ParticipantEntity> participants;

    public ConversationEntity(String conversationName) {
        this.conversationName = conversationName;
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

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        long now = Instant.now().toEpochMilli();
        this.createdAt = now;
    }
}
