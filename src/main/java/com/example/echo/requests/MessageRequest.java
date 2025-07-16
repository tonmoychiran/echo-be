package com.example.echo.requests;

import jakarta.validation.constraints.NotBlank;

public class MessageRequest {
    @NotBlank(message = "Conversation Id is empty")
    String conversationId;

    @NotBlank(message = "Message is empty")
    String message;

    public String getMessage() {
        return message;
    }

    public String getConversationId() {
        return conversationId;
    }
}
