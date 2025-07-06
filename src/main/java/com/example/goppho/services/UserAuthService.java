package com.example.goppho.services;

import com.example.goppho.requests.UserLoginOTPRequest;
import com.example.goppho.requests.UserLoginOTPResendRequest;
import com.example.goppho.responses.Response;
import com.example.goppho.responses.UserLoginOTPResponse;
import com.example.goppho.requests.UserLoginVerificationRequest;
import com.example.goppho.entities.UserAuthOTPEntity;
import com.example.goppho.entities.UserEntity;
import com.example.goppho.repositories.UserAuthOTPRepository;
import com.example.goppho.repositories.UserRepository;
import com.example.goppho.responses.VerifiedUserLoginResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserAuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    UserAuthOTPRepository userAuthOTPRepository;
    UserRepository userRepository;
    EmailSenderService emailSenderService;

    @Autowired
    public UserAuthService(
            AuthenticationManager authenticationManager,
            UserAuthOTPRepository userAuthOTPRepository,
            UserRepository userRepository,
            JwtService jwtService,
            EmailSenderService emailSenderService
    ) {
        this.authenticationManager = authenticationManager;
        this.userAuthOTPRepository = userAuthOTPRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.emailSenderService = emailSenderService;
    }

    @Transactional
    public Response generateUserLoginOTP(
            UserLoginOTPRequest userLoginRequest
    ) {
        String email = userLoginRequest.getEmail();

        Optional<UserEntity> existingUser = this.userRepository.findByUserEmail(email);
        UserEntity userEntity;
        if (existingUser.isEmpty()) {
            UserEntity newUser = new UserEntity(email);
            userEntity = this.userRepository.save(newUser);
        } else {
            userEntity = existingUser.get();
        }

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
    public VerifiedUserLoginResponse verifyUserLoginOTP(
            UserLoginVerificationRequest userLoginVerifyRequest
    ) {
        String email = userLoginVerifyRequest.getEmail();
        String requestedOTP = userLoginVerifyRequest.getOtp();

        Optional<UserEntity> existingUser = this.userRepository.findByUserEmail(email);
        if (existingUser.isEmpty())
            throw new EntityNotFoundException("User not found");

        Authentication authentication = authenticationManager.authenticate(
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
