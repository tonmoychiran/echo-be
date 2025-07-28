package com.example.echo.requests;

import com.example.echo.annotations.ValidUsername;
import jakarta.validation.constraints.NotBlank;

public class ConnectionRequest {
    @NotBlank(message = "Username is empty")
    @ValidUsername
    String username;

    public String getUsername() {
        return username;
    }
}
