package com.example.goppho.controllers;

import com.example.goppho.entities.UserEntity;
import com.example.goppho.interfaces.UsernameValidator;
import com.example.goppho.responses.GetResponse;
import com.example.goppho.services.UserService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.apache.coyote.BadRequestException;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    UserService userService;

    @Autowired
    public UserController(
            UserService userService
    ) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<GetResponse<UserEntity>> getUserProfile(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        GetResponse<UserEntity> response = this.userService.getUserProfile(userDetails);
        return ResponseEntity.ok(
                response
        );
    }

    @GetMapping("/check/email")
    public ResponseEntity<Void> checkEmail(
            @NotBlank(message = "Email is empty")
            @Length(max = 50, message = "Email exceeds 50 characters")
            @Email(message = "Enter a valid email")
            @RequestParam String email
    ) throws BadRequestException {
        this.userService.checkEmail(email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check/username")
    public ResponseEntity<Void> checkUsername(
            @NotBlank(message = "Username is empty")
            @Length(max = 50, message = "Username exceeds 50 characters")
            @UsernameValidator
            @RequestParam String username
    ) throws BadRequestException {
        this.userService.checkUsername(username);
        return ResponseEntity.ok().build();
    }
}
