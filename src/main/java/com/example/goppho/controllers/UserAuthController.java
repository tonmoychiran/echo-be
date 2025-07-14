package com.example.goppho.controllers;


import com.example.goppho.requests.UserLoginOTPRequest;
import com.example.goppho.responses.Response;
import com.example.goppho.requests.UserLoginVerificationRequest;
import com.example.goppho.responses.VerifiedUserLoginResponse;
import com.example.goppho.services.UserAuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class UserAuthController {

    private final UserAuthService userAuthService;

    @Autowired
    public UserAuthController(
            UserAuthService userAuthService
    ) {
        this.userAuthService = userAuthService;
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(
            @Valid @RequestBody UserLoginOTPRequest userLoginRequest
    ) {
        Response response = this.userAuthService.login(
                userLoginRequest
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/verification")
    public ResponseEntity<VerifiedUserLoginResponse> verifyOtp(
            @Valid @RequestBody UserLoginVerificationRequest userLoginVerifyRequest
    ) {
        VerifiedUserLoginResponse response = this.userAuthService.verifyOtp(
                userLoginVerifyRequest
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
