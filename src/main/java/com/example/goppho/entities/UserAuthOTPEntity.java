package com.example.goppho.entities;

import com.example.goppho.services.SecureOTP;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "user_auth_otp")
public class UserAuthOTPEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36)
    private String otpId;

    @Column(length = 6, unique = true, nullable = false)
    private String otp;

    @Column(nullable = false)
    private Long createdAt;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    public UserAuthOTPEntity(UserEntity user) {
        this.user = user;
    }

    public UserAuthOTPEntity() {
    }

    public UserEntity getUser() {
        return user;
    }

    public String getOtpId() {
        return otpId;
    }

    public String getOtp() {
        return otp;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    @PrePersist
    protected void onCreate() {
        this.otp = SecureOTP.generateOTP(6);
        this.createdAt = Instant.now().toEpochMilli();
    }

}
