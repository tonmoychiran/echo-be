package com.example.goppho.entities;

import com.example.goppho.enums.ConnectionRequestStatusEnum;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "connection_request",
        uniqueConstraints = @UniqueConstraint(columnNames = {"sender_user_information_id", "receiver_user_information_id"}))
public class ConnectionRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String connectionRequestId;

    @ManyToOne
    @JoinColumn(name = "sender_user_information_id", nullable = false)
    private UserInformationEntity sender;

    @ManyToOne
    @JoinColumn(name = "receiver_user_information_id", nullable = false)
    private UserInformationEntity receiver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConnectionRequestStatusEnum status = ConnectionRequestStatusEnum.PENDING;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Long createdAt;

    @Column(name = "updated_at", nullable = false)
    private Long lastUpdatedAt;

    public ConnectionRequestEntity() {
    }

    public ConnectionRequestEntity(UserInformationEntity sender, UserInformationEntity receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    public String getConnectionRequestId() {
        return connectionRequestId;
    }

    public void setConnectionRequestId(String connectionRequestId) {
        this.connectionRequestId = connectionRequestId;
    }

    public UserInformationEntity getSender() {
        return sender;
    }

    public void setSender(UserInformationEntity sender) {
        this.sender = sender;
    }

    public UserInformationEntity getReceiver() {
        return receiver;
    }

    public void setReceiver(UserInformationEntity receiver) {
        this.receiver = receiver;
    }

    public ConnectionRequestStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ConnectionRequestStatusEnum status) {
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
        if (sender != null && receiver != null && sender.getUser().equals(receiver.getUser())) {
            throw new IllegalArgumentException("Sender and receiver cannot be the same user");
        }
    }

}
