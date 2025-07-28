package com.example.echo.entities;

import jakarta.persistence.*;

@Entity
@Table(
        name = "message_attachment",
        uniqueConstraints = @UniqueConstraint(columnNames = {"message_id", "attachment_id"})
)
public class MessageAttachmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long messageAttachmentId;

    @ManyToOne
    @JoinColumn(name = "attachment_id")
    private AttachmentEntity attachment;

    @ManyToOne
    @JoinColumn(name = "message_id")
    private MessageEntity message;

    public MessageAttachmentEntity(AttachmentEntity attachment, MessageEntity message) {
        this.attachment = attachment;
        this.message = message;
    }

    public MessageAttachmentEntity() {
    }

    public AttachmentEntity getAttachment() {
        return attachment;
    }

    public void setAttachment(AttachmentEntity attachment) {
        this.attachment = attachment;
    }

    public MessageEntity getMessage() {
        return message;
    }

    public void setMessage(MessageEntity message) {
        this.message = message;
    }
}
