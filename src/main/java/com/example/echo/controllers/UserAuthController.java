package com.example.echo.controllers;


import com.example.echo.requests.UserLoginOTPRequest;
import com.example.echo.responses.Response;
import com.example.echo.requests.UserLoginVerificationRequest;
import com.example.echo.responses.VerifiedUserLoginResponse;
import com.example.echo.services.UserAuthService;
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
