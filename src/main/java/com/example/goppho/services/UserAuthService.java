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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
public class UserAuthService {
    private final JwtService jwtService;
    UserAuthOTPRepository userAuthOTPRepository;
    UserRepository userRepository;
    EmailSenderService emailSenderService;

    @Autowired
    public UserAuthService(
            UserAuthOTPRepository userAuthOTPRepository,
            UserRepository userRepository,
            JwtService jwtService,
            EmailSenderService emailSenderService
    ) {
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
            throw new EntityNotFoundException("Invalid request");
        UserEntity userEntity = existingUser.get();
        String userId = userEntity.getUserId();

        Optional<UserAuthOTPEntity> otpEntity =
                this.userAuthOTPRepository.findFirstByUserOrderByCreatedAtDesc(userId);
        if (otpEntity.isEmpty())
            throw new EntityNotFoundException("Invalid request");

        UserAuthOTPEntity savedOTPEntity = otpEntity.get();
        String savedOTP = savedOTPEntity.getOtp();
        Long savedOTPAt = savedOTPEntity.getCreatedAt();

        if (isOTPExpired(savedOTPAt))
            throw new CredentialsExpiredException("OTP expired");

        if (!requestedOTP.equals(savedOTP))
            throw new BadCredentialsException("OTP not matched");

        String accessToken = this.jwtService.generateToken(userId);
        long expiresIn = this.jwtService.getValidation();

        return new VerifiedUserLoginResponse(
                "Login successful",
                accessToken,
                expiresIn
        );
    }

    private Boolean isOTPExpired(Long savedOTPAt) {
        if (savedOTPAt == null)
            return true;

        long now = Instant.now().toEpochMilli();
        long fiveMinutesInMillis = Duration.ofMinutes(5).toMillis();

        return (savedOTPAt + fiveMinutesInMillis) < now;
    }
}
