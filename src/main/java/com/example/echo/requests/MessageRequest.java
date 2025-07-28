package com.example.echo.requests;

import com.example.echo.entities.AttachmentEntity;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class MessageRequest {
    List<AttachmentEntity> attachments;

    @NotBlank(message = "Message is empty")
    String message;

    public String getMessage() {
        return message;
    }

    public List<AttachmentEntity> getAttachments() {
        return attachments;
    }
}
