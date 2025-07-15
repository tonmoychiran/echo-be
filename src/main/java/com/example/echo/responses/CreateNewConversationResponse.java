package com.example.echo.responses;

import com.example.echo.entities.ConversationEntity;

public class CreateNewConversationResponse extends Response{

    private ConversationEntity conversation;

    public CreateNewConversationResponse(String message, ConversationEntity conversation) {
        super(message);
        this.conversation = conversation;
    }
}
