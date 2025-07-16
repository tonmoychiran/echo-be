package com.example.echo.responses;

import com.example.echo.entities.UserEntity;

public class MessageResponse {
    private String messageId;
    private String message;
    private Long createdAt;
    private UserEntity user;
    private String conversationId;

    public MessageResponse(String messageId, String message, Long createdAt, UserEntity user, String conversationId) {
        this.messageId = messageId;
        this.message = message;
        this.createdAt = createdAt;
        this.user = user;
        this.conversationId = conversationId;
    }

    // Getters and setters
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

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
