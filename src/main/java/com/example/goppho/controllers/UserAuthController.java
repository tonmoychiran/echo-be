package com.example.goppho.controllers;

import com.example.goppho.entities.UserPrincipalEntity;
import com.example.goppho.requests.UserLoginOTPRequest;
import com.example.goppho.requests.UserLoginOTPResendRequest;
import com.example.goppho.responses.Response;
import com.example.goppho.responses.UserLoginOTPResponse;
import com.example.goppho.requests.UserLoginVerificationRequest;
import com.example.goppho.responses.VerifiedUserLoginResponse;
import com.example.goppho.services.UserAuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class UserAuthController {

    private final UserAuthService userAuthService;

    @Autowired
    public UserAuthController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @PostMapping("/login")
    public ResponseEntity<Response> generateUserLoginOTP(
            @Valid @RequestBody UserLoginOTPRequest userLoginRequest
    ) {
        Response response = this.userAuthService.generateUserLoginOTP(
                userLoginRequest
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/verification")
    public ResponseEntity<VerifiedUserLoginResponse> verifyUserLoginOTP(
            @Valid @RequestBody UserLoginVerificationRequest userLoginVerifyRequest
    ) {
        VerifiedUserLoginResponse response = this.userAuthService.verifyUserLoginOTP(
                userLoginVerifyRequest
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserPrincipalEntity> getUserProfile(@AuthenticationPrincipal UserPrincipalEntity userPrincipal) {

/*        if (userPrincipal == null) {
            return ResponseEntity.badRequest().body("User not authenticated.");
        }

        String userId = userPrincipal.getUsername(); // Get the user ID
        //Or any other information from the userPrincipal.*/
        return ResponseEntity.ok(userPrincipal);
//        {"enabled":true,"accountNonLocked":true,"username":"46f1c84d-674c-4232-af34-ec80132cdf75","authorities":[],"credentialsNonExpired":true,"accountNonExpired":true,"password":""}
    }
}
