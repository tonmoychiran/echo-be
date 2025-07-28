package com.example.echo.entities;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name="attachment")
public class AttachmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long attachmentId;

    @Column
    private String attachment;

    @Column
    private Long createdAt;

    public AttachmentEntity(String attachment) {
        this.attachment = attachment;
    }

    public AttachmentEntity() {

    }

    public Long getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(Long attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now().toEpochMilli();
    }

}
