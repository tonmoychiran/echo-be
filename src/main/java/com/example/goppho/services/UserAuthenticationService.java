package com.example.goppho.services;

import com.example.goppho.requests.UserLoginOTPRequest;
import com.example.goppho.requests.UserLoginOTPResendRequest;
import com.example.goppho.responses.UserLoginOTPResponse;
import com.example.goppho.requests.UserLoginVerificationRequest;
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
    public UserLoginOTPResponse requestUserLoginVerificationOTP(
            UserLoginOTPRequest userLoginRequest
    ) {
        String email = userLoginRequest.getEmail();

        UserEntity userEntity = new UserEntity(email);
        UserAuthOTPEntity otpEntity = new UserAuthOTPEntity(userEntity);

        this.userRepository.save(userEntity);
        UserAuthOTPEntity savedOtp = this.userAuthOTPRepository.save(otpEntity);

        return new UserLoginOTPResponse(
                "Otp sent",
                savedOtp.getOtpId()
        );
    }

    @Transactional
    public UserEntity verifyUserLoginOTP(
            UserLoginVerificationRequest userLoginVerifyRequest
    ) {
        String otpId = userLoginVerifyRequest.getOtpId();
        String requestedOTP = userLoginVerifyRequest.getOtp();

        Optional<UserAuthOTPEntity> otpEntity = this.userAuthOTPRepository.findById(otpId);
        if (otpEntity.isEmpty())
            throw new EntityNotFoundException("Request not found");

        UserAuthOTPEntity savedOTPEntity = otpEntity.get();
        String savedOTP = savedOTPEntity.getOtp();
        Long savedOTPAt = savedOTPEntity.getCreatedAt();

        if (isOTPExpired(savedOTPAt))
            throw new CredentialsExpiredException("OTP expired");

        if (!requestedOTP.equals(savedOTP))
            throw new BadCredentialsException("OTP not matched");

        this.userAuthOTPRepository.deleteById(otpId);
        return savedOTPEntity.getUser();
    }

    @Transactional
    public UserLoginOTPResponse resendUserLoginVerificationOTP(
            UserLoginOTPResendRequest userLoginOTPResendRequest
    ) {
        String otpId = userLoginOTPResendRequest.getOtpId();

        Optional<UserAuthOTPEntity> otpEntity = this.userAuthOTPRepository.findById(otpId);
        if (otpEntity.isEmpty())
            throw new EntityNotFoundException("Request not found");
        UserAuthOTPEntity savedOTPEntity = otpEntity.get();
        UserEntity userEntity = savedOTPEntity.getUser();

        UserAuthOTPEntity newOtpEntity = new UserAuthOTPEntity(userEntity);
        UserAuthOTPEntity newSavedOTPEntity = this.userAuthOTPRepository.save(newOtpEntity);

        this.userAuthOTPRepository.deleteById(otpId);

        return new UserLoginOTPResponse(
                "Otp sent",
                newSavedOTPEntity.getOtpId()
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
