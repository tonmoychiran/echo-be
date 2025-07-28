package com.example.echo.requests;

import com.example.echo.annotations.ValidUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class UserRegistrationRequest {
    @NotBlank(message = "Name is empty")
    @Length(max = 50, message = "Name exceeds 50 characters")
    String name;

    @NotNull(message="Date of Birth is empty")
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    @Past(message = "Invalid Date Of Birth")
    LocalDate dob;

    @NotBlank(message = "Email is empty")
    @Length(max = 50, message = "Email exceeds 50 characters")
    @Email(message = "Enter a valid email")
    String email;

    @NotBlank(message = "Username is empty")
    @Length(max = 50, message = "Username exceeds 50 characters")
    @ValidUsername
    String username;

    public @NotBlank(message = "Name is empty") @Length(max = 50, message = "Name exceeds 50 characters") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Name is empty") @Length(max = 50, message = "Name exceeds 50 characters") String name) {
        this.name = name;
    }

    public @NotNull(message = "Date of Birth is empty") @Past(message = "Invalid Date Of Birth") LocalDate getDob() {
        return dob;
    }

    public void setDob(@NotNull(message = "Date of Birth is empty") @Past(message = "Invalid Date Of Birth") LocalDate dob) {
        this.dob = dob;
    }

    public @NotBlank(message = "Email is empty") @Length(max = 50, message = "Email exceeds 50 characters") @Email(message = "Enter a valid email") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "Email is empty") @Length(max = 50, message = "Email exceeds 50 characters") @Email(message = "Enter a valid email") String email) {
        this.email = email;
    }

    public @NotBlank(message = "Username is empty") @Length(max = 50, message = "Username exceeds 50 characters") String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank(message = "Username is empty") @Length(max = 50, message = "Username exceeds 50 characters") String username) {
        this.username = username;
    }
}
