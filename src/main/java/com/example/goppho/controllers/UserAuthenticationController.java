package com.example.goppho.controllers;


import com.example.goppho.dtos.UserLoginRequestDTO;
import com.example.goppho.dtos.UserLoginVerifyRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserAuthenticationController {

    @PostMapping("/login")
    public ResponseEntity<UserLoginRequestDTO> requestLogin(
            @Valid @RequestBody UserLoginRequestDTO userLoginRequest
    ) {
        return new ResponseEntity<UserLoginRequestDTO>(userLoginRequest, HttpStatus.CREATED);
    }

    @PatchMapping("/login/verify")
    public ResponseEntity<UserLoginVerifyRequestDTO> verifyLogin(
            @Valid @RequestBody UserLoginVerifyRequestDTO user
    ) {
        return new ResponseEntity<UserLoginVerifyRequestDTO>(user, HttpStatus.CREATED);
    }
}
