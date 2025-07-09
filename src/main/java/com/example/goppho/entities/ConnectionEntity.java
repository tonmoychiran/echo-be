package com.example.goppho.entities;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "connection",
        uniqueConstraints = @UniqueConstraint(columnNames = {"head_user_id", "tail_user_id"}))
public class ConnectionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String connectionId;

    @ManyToOne
    @JoinColumn(name = "head_user_id", nullable = false)
    private UserEntity head;

    @ManyToOne
    @JoinColumn(name = "tail_user_id", nullable = false)
    private UserEntity tail;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Long createdAt;

    @Column(name = "updated_at", nullable = false)
    private Long lastUpdatedAt;

    public ConnectionEntity(UserEntity head, UserEntity tail) {
        this.head = head;
        this.tail = tail;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public UserEntity getHead() {
        return head;
    }

    public void setHead(UserEntity head) {
        this.head = head;
    }

    public UserEntity getTail() {
        return tail;
    }

    public void setTail(UserEntity tail) {
        this.tail = tail;
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
        if (head != null && tail != null && head.getUserId().equals(tail.getUserId())) {
            throw new IllegalArgumentException("User can not be connected with themselves");
        }
    }
}
