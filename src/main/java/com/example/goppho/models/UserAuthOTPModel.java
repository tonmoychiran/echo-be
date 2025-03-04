package com.example.goppho.models;

import com.example.goppho.services.SecureOTP;
import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;

import java.time.Instant;

public class UserAuthOTPModel {
    @Column(length = 50, unique = true, nullable = false)
    private String userEmail;

    @Column(length = 6, unique = true, nullable = false)
    private String otp;

    @Column(nullable = false)
    private Long createdAt;

    @PrePersist
    protected void onCreate() {
        this.otp = SecureOTP.generateOTP(6);
        this.createdAt = Instant.now().toEpochMilli();
    }

    public UserAuthOTPModel(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
}
