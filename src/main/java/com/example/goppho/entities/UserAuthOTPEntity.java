package com.example.goppho.entities;

import com.example.goppho.services.SecureOTP;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;

import java.time.Instant;

public class UserAuthOTPEntity {
    @Id
    @Column(length = 50, nullable = false)
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

    public UserAuthOTPEntity(String userEmail) {
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
