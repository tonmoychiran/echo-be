package com.example.echo.entities;

import jakarta.persistence.*;

import java.sql.Date;
import java.time.Instant;

@Entity
@Table(name = "users",
indexes = {
        @Index(name = "idx_users_username", columnList = "username"),
        @Index(name = "idx_users_email", columnList = "email"),
        @Index(name = "idx_users_name", columnList = "name")
})
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36)
    private String userId;

    @Column(length = 50, unique = true, nullable = false)
    private String username;

    @Column(length = 50, unique = true, nullable = false)
    private String email;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(nullable = false)
    private Date dob;

    @Column(nullable = false, updatable = false)
    private Long createdAt;

    @Column(nullable = false)
    private Long lastUpdatedAt;

    public UserEntity(String username, String email, String name, Date dob) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.dob = dob;
    }

    public UserEntity() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(Long lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    @PrePersist
    protected void onCreate() {
        long now = Instant.now().toEpochMilli();
        this.createdAt = now;
        this.lastUpdatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastUpdatedAt = Instant.now().toEpochMilli();
    }
}
