package com.example.echo.requests;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class UserNameUpdateRequest {
    @NotBlank(message = "Name is empty")
    @Length(max = 50, message = "Name must be no longer than 50 characters")
    String name;

    public String getName() {
        return name;
    }
}
