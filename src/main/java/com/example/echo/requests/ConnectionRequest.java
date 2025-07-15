package com.example.echo.requests;

import com.example.echo.interfaces.UsernameValidator;
import jakarta.validation.constraints.NotBlank;

public class ConnectionRequest {
    @NotBlank(message = "Username is empty")
    @UsernameValidator
    String username;

    public String getUsername() {
        return username;
    }
}
