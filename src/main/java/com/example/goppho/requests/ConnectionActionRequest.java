package com.example.goppho.requests;

import jakarta.validation.constraints.NotBlank;

public class ConnectionActionRequest {
    @NotBlank(message = "Connection request id is empty")
    String connectionRequestId;

    public String getConnectionRequestId() {
        return connectionRequestId;
    }
}
