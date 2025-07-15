package com.example.echo.requests;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

public class UserLoginOTPRequest {
    @NotBlank(message = "Email is empty")
    @Length(max = 50, message = "Email exceeds 50 characters")
    @Email(message = "Enter a valid email")
    String email;

    public  String getEmail() {
        return email;
    }
}
