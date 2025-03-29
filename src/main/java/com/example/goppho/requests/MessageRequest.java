package com.example.goppho.requests;

import jakarta.validation.constraints.NotBlank;

public class MessageRequest {
    @NotBlank(message = "Message is empty")
    String message;

    public String getMessage() {
        return message;
    }
}
