package com.example.echo.entities;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "message")
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String messageId;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column
    private Long createdAt;

    @OneToMany(mappedBy = "message")
    private List<MessageMediaEntity> media;

    @ManyToOne
    @JoinColumn(name="user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name="conversation_id")
    private ConversationEntity conversation;

    public MessageEntity(String message, UserEntity user, ConversationEntity conversation) {
        this.message = message;
        this.user = user;
        this.conversation = conversation;
    }

    public MessageEntity(String message, List<MessageMediaEntity> media, UserEntity user, ConversationEntity conversation) {
        this.message = message;
        this.media = media;
        this.user = user;
        this.conversation = conversation;
    }

    public MessageEntity() {

    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public List<MessageMediaEntity> getMedia() {
        return media;
    }

    public void setMedia(List<MessageMediaEntity> media) {
        this.media = media;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public ConversationEntity getConversation() {
        return conversation;
    }

    public void setConversation(ConversationEntity conversation) {
        this.conversation = conversation;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now().toEpochMilli();
    }
}
