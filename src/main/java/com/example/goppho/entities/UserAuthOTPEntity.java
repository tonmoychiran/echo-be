package com.example.goppho.entities;

import com.example.goppho.services.SecureOTP;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.List;

@Entity
@IdClass(UserAuthOTPID.class)
@Table(name = "user_auth_otp")
public class UserAuthOTPEntity implements UserDetails {
    @Column(length = 6, unique = true, nullable = false)
    private String otp;

    @Id
    @Column(nullable = false)
    private Long createdAt;

    @Id
    @ManyToOne
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.otp;
    }

    @Override
    public String getUsername() {
        return this.user.getUserEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        long now = Instant.now().toEpochMilli();
        long fiveMinutesInMillis = Duration.ofMinutes(5).toMillis();

        return (now - this.createdAt) < fiveMinutesInMillis;
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
