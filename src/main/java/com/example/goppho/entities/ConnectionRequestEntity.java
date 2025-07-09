package com.example.goppho.entities;

import com.example.goppho.ConnectionRequestStatus;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "connection_request",
        uniqueConstraints = @UniqueConstraint(columnNames = {"sender_id", "receiver_id"}))
public class ConnectionRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String connectionRequestId;

    @ManyToOne
    @JoinColumn(name = "sender_user_id", nullable = false)
    private UserEntity sender;

    @ManyToOne
    @JoinColumn(name = "receiver_user_id", nullable = false)
    private UserEntity receiver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "ENUM('pending', 'accepted', 'rejected') DEFAULT 'pending'")
    private ConnectionRequestStatus status = ConnectionRequestStatus.PENDING;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Long createdAt;

    @Column(name = "updated_at", nullable = false)
    private Long lastUpdatedAt;

    public ConnectionRequestEntity(UserEntity sender, UserEntity receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    public String getConnectionRequestId() {
        return connectionRequestId;
    }

    public void setConnectionRequestId(String connectionRequestId) {
        this.connectionRequestId = connectionRequestId;
    }

    public UserEntity getSender() {
        return sender;
    }

    public void setSender(UserEntity sender) {
        this.sender = sender;
    }

    public UserEntity getReceiver() {
        return receiver;
    }

    public void setReceiver(UserEntity receiver) {
        this.receiver = receiver;
    }

    public ConnectionRequestStatus getStatus() {
        return status;
    }

    public void setStatus(ConnectionRequestStatus status) {
        this.status = status;
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
        if (sender != null && receiver != null && sender.getUserId().equals(receiver.getUserId())) {
            throw new IllegalArgumentException("Sender and receiver cannot be the same user");
        }
    }

}
