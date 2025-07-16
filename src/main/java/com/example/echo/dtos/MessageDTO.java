package com.example.echo.dtos;

import com.example.echo.entities.UserEntity;
import com.example.echo.enums.PublishableEnum;
import com.example.echo.interfaces.Publishable;

public class MessageDTO implements Publishable {
    private String messageId;
    private String message;
    private Long createdAt;
    private UserEntity user;
    private String conversationId;

    public MessageDTO(String messageId, String message, Long createdAt, UserEntity user, String conversationId) {
        this.messageId = messageId;
        this.message = message;
        this.createdAt = createdAt;
        this.user = user;
        this.conversationId = conversationId;
    }

    // Getters and setters
    public PublishableEnum getType() {
        return PublishableEnum.MESSAGE;
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
