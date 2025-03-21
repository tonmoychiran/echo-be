package com.example.goppho.controllers;

import com.example.goppho.entities.UserPrincipalEntity;
import com.example.goppho.requests.UserLoginOTPRequest;
import com.example.goppho.requests.UserLoginOTPResendRequest;
import com.example.goppho.responses.UserLoginOTPResponse;
import com.example.goppho.requests.UserLoginVerificationRequest;
import com.example.goppho.entities.UserEntity;
import com.example.goppho.responses.VerifiedUserLoginResponse;
import com.example.goppho.services.JwtService;
import com.example.goppho.services.UserAuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class UserAuthenticationController {

    private final UserAuthenticationService userAuthenticationService;
    private final JwtService jwtService;

    @Autowired
    public UserAuthenticationController(UserAuthenticationService userAuthenticationService, JwtService jwtService) {
        this.userAuthenticationService = userAuthenticationService;
        this.jwtService = jwtService;
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
    public ResponseEntity<VerifiedUserLoginResponse> verifyUserLoginOTP(
            @Valid @RequestBody UserLoginVerificationRequest userLoginVerifyRequest
    ) {
        VerifiedUserLoginResponse response = this.userAuthenticationService.verifyUserLoginOTP(
                userLoginVerifyRequest
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserPrincipalEntity> getUserProfile(@AuthenticationPrincipal UserPrincipalEntity userPrincipal) {

//        if (userPrincipal == null) {
//            return ResponseEntity.badRequest().body("User not authenticated.");
//        }
//
//        String userId = userPrincipal.getUsername(); // Get the user ID
//        //Or any other information from the userPrincipal.
        return ResponseEntity.ok(userPrincipal);
//        {"enabled":true,"accountNonLocked":true,"username":"46f1c84d-674c-4232-af34-ec80132cdf75","authorities":[],"credentialsNonExpired":true,"accountNonExpired":true,"password":""}
    }
}
