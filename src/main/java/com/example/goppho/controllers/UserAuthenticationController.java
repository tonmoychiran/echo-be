package com.example.goppho.controllers;

import com.example.goppho.requests.UserLoginOTPRequest;
import com.example.goppho.requests.UserLoginOTPResendRequest;
import com.example.goppho.responses.UserLoginOTPResponse;
import com.example.goppho.requests.UserLoginVerificationRequest;
import com.example.goppho.entities.UserEntity;
import com.example.goppho.services.UserAuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class UserAuthenticationController {

    private final UserAuthenticationService userAuthenticationService;

    @Autowired
    public UserAuthenticationController(UserAuthenticationService userAuthenticationService) {
        this.userAuthenticationService = userAuthenticationService;
    }

    @PostMapping("/login/verification/otp")
    public ResponseEntity<UserLoginOTPResponse> requestUserLoginVerificationOTP(
            @Valid @RequestBody UserLoginOTPRequest userLoginRequest
    ) {
        UserLoginOTPResponse response = this.userAuthenticationService.requestUserLoginVerificationOTP(
                userLoginRequest
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PostMapping("/login/verification/otp/resend")
    public ResponseEntity<UserLoginOTPResponse> resendUserLoginVerificationOTP(
            @Valid @RequestBody UserLoginOTPResendRequest userLoginOTPResendRequest
    ) {
        UserLoginOTPResponse response = this.userAuthenticationService.resendUserLoginVerificationOTP(
                userLoginOTPResendRequest
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login/verification")
    public ResponseEntity<UserEntity> verifyUserLoginOTP(
            @Valid @RequestBody UserLoginVerificationRequest userLoginVerifyRequest
    ) {
        UserEntity response = this.userAuthenticationService.verifyUserLoginOTP(
                userLoginVerifyRequest
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
