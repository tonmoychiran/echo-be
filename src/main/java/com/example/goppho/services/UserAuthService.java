package com.example.goppho.services;

import com.example.goppho.requests.UserLoginOTPRequest;
import com.example.goppho.responses.Response;
import com.example.goppho.requests.UserLoginVerificationRequest;
import com.example.goppho.entities.UserAuthOTPEntity;
import com.example.goppho.entities.UserEntity;
import com.example.goppho.repositories.UserAuthOTPRepository;
import com.example.goppho.responses.VerifiedUserLoginResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserAuthService {
    UserService userService;
    private final JwtService jwtService;
    EmailSenderService emailSenderService;
    UserAuthOTPRepository userAuthOTPRepository;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserAuthService(
            UserService userService,
            JwtService jwtService,
            EmailSenderService emailSenderService,
            UserAuthOTPRepository userAuthOTPRepository,
            AuthenticationManager authenticationManager
    ) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.emailSenderService = emailSenderService;
        this.userAuthOTPRepository = userAuthOTPRepository;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public Response login(
            UserLoginOTPRequest userLoginRequest
    ) {
        String email = userLoginRequest.getEmail();

        Optional<UserEntity> existingUser = this.userService.getUserByEmail(email);

        if (existingUser.isEmpty()) {
            throw new EntityNotFoundException("User not found");
        }
        UserEntity userEntity = existingUser.get();

        UserAuthOTPEntity otpEntity = new UserAuthOTPEntity(userEntity);
        UserAuthOTPEntity savedOtp = this.userAuthOTPRepository.save(otpEntity);
        String otp = savedOtp.getOtp();

        String subject = "Your Login OTP is " + otp;
        emailSenderService.sendEmail(email, subject, subject);

        return new Response(
                "Otp sent"
        );
    }

    @Transactional
    public VerifiedUserLoginResponse verifyOtp(
            UserLoginVerificationRequest userLoginVerifyRequest
    ) {
        String email = userLoginVerifyRequest.getEmail();
        String requestedOTP = userLoginVerifyRequest.getOtp();

        Optional<UserEntity> existingUser = this.userService.getUserByEmail(email);
        if (existingUser.isEmpty())
            throw new EntityNotFoundException("User not found");

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, requestedOTP)
        );

        String accessToken = this.jwtService.generateToken(email);
        long expiresIn = this.jwtService.getValidation();

        return new VerifiedUserLoginResponse(
                "Login successful",
                accessToken,
                expiresIn
        );
    }
}
