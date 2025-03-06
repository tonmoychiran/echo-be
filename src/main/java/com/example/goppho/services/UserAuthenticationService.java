package com.example.goppho.services;

import com.example.goppho.dtos.UserLoginRequestDTO;
import com.example.goppho.dtos.UserLoginRequestSuccessfulResponseDTO;
import com.example.goppho.dtos.UserLoginVerifyRequestDTO;
import com.example.goppho.entities.UserAuthOTPEntity;
import com.example.goppho.entities.UserEntity;
import com.example.goppho.repositories.UserAuthOTPRepository;
import com.example.goppho.repositories.UserRepository;
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
public class UserAuthenticationService {
    UserAuthOTPRepository userAuthOTPRepository;
    UserRepository userRepository;

    @Autowired
    public UserAuthenticationService(
            UserAuthOTPRepository userAuthOTPRepository,
            UserRepository userRepository
    ) {
        this.userAuthOTPRepository = userAuthOTPRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public UserLoginRequestSuccessfulResponseDTO requestLogin(
            UserLoginRequestDTO userLoginRequest
    ) {
        String email = userLoginRequest.getEmail();

        UserEntity userEntity = new UserEntity(email);
        UserAuthOTPEntity otpEntity = new UserAuthOTPEntity(userEntity);

        this.userRepository.save(userEntity);
        UserAuthOTPEntity savedOtp = this.userAuthOTPRepository.save(otpEntity);

        return new UserLoginRequestSuccessfulResponseDTO(
                "Otp sent",
                savedOtp.getOtpId()
        );
    }

    @Transactional
    public UserEntity verifyLogin(
            UserLoginVerifyRequestDTO userLoginVerifyRequest
    ) {
        String otpId = userLoginVerifyRequest.getId();
        String requestedOTP = userLoginVerifyRequest.getOtp();

        Optional<UserAuthOTPEntity> otpEntity = this.userAuthOTPRepository.findById(otpId);
        if (otpEntity.isEmpty())
            throw new EntityNotFoundException("Request not found");

        UserAuthOTPEntity savedOTPEntity = otpEntity.get();
        String savedOTP = savedOTPEntity.getOtp();
        Long savedOTPAt = savedOTPEntity.getCreatedAt();

        if (isOTPExpired(savedOTPAt))
            throw new CredentialsExpiredException("OTP expired");

        if (requestedOTP.equals(savedOTP))
            return savedOTPEntity.getUser();

        throw new BadCredentialsException("Bad credentials");
    }

    private Boolean isOTPExpired(Long savedOTPAt) {
        if (savedOTPAt == null)
            return true;

        long now = Instant.now().toEpochMilli();
        long fiveMinutesInMillis = Duration.ofMinutes(5).toMillis();

        return (savedOTPAt + fiveMinutesInMillis) < now;
    }
}
