package com.example.echo.entities;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "connection",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "friend_id"}),
        indexes = {
                @Index(name = "idx_connection_user_id", columnList = "user_id"),
                @Index(name = "idx_connection_friend_id", columnList = "friend_id"),
        })
public class ConnectionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String connectionId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "friend_id", nullable = false)
    private UserEntity friend;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Long createdAt;

    @Column(name = "updated_at", nullable = false)
    private Long lastUpdatedAt;

    public ConnectionEntity() {
    }

    public ConnectionEntity(UserEntity user, UserEntity friend) {
        this.user = user;
        this.friend = friend;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public UserEntity getHead() {
        return user;
    }

    public void setHead(UserEntity user) {
        this.user = user;
    }

    public UserEntity getTail() {
        return friend;
    }

    public void setTail(UserEntity friend) {
        this.friend = friend;
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
        this.validateSenderReceiver();
        long now = Instant.now().toEpochMilli();
        this.createdAt = now;
        this.lastUpdatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.validateSenderReceiver();
        this.lastUpdatedAt = Instant.now().toEpochMilli();
    }

    public void validateSenderReceiver() {
        if (user != null && user.equals(friend)) {
            throw new IllegalArgumentException("User can not be connected with themselves");
        }
    }
}
