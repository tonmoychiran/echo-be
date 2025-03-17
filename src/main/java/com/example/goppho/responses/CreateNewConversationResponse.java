package com.example.goppho.responses;

import com.example.goppho.entities.ConversationEntity;

public class CreateNewConversationResponse extends Response{

    private ConversationEntity conversation;

    public CreateNewConversationResponse(String message, ConversationEntity conversation) {
        super(message);
        this.conversation = conversation;
    }
}
