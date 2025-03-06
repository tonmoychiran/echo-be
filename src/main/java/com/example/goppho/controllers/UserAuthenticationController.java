package com.example.goppho.controllers;

import com.example.goppho.dtos.UserLoginRequestDTO;
import com.example.goppho.dtos.UserLoginRequestSuccessfulResponseDTO;
import com.example.goppho.dtos.UserLoginVerifyRequestDTO;
import com.example.goppho.entities.UserEntity;
import com.example.goppho.services.UserAuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserAuthenticationController {

    private final UserAuthenticationService userAuthenticationService;

    @Autowired
    public UserAuthenticationController(UserAuthenticationService userAuthenticationService) {
        this.userAuthenticationService = userAuthenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginRequestSuccessfulResponseDTO> requestLogin(
            @Valid @RequestBody UserLoginRequestDTO userLoginRequest
    ) {
        UserLoginRequestSuccessfulResponseDTO response = this.userAuthenticationService.requestLogin(
                userLoginRequest
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/login/verify")
    public ResponseEntity<UserEntity> verifyLogin(
            @Valid @RequestBody UserLoginVerifyRequestDTO userLoginVerifyRequest
    ) {
        UserEntity response = this.userAuthenticationService.verifyLogin(
                userLoginVerifyRequest
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
