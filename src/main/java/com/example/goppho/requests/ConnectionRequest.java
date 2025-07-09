package com.example.goppho.requests;

import jakarta.validation.constraints.NotBlank;

public class ConnectionRequest {
    @NotBlank(message = "User id is empty")
    String userId;

    public String getUserId() {
        return userId;
    }
}
