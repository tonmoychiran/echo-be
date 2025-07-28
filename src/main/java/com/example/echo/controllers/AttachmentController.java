package com.example.echo.controllers;

import com.example.echo.annotations.ValidFile;
import com.example.echo.entities.AttachmentEntity;
import com.example.echo.services.MessageService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/attachment")
public class AttachmentController {

    private final MessageService messageService;

    @Autowired
    public AttachmentController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/{conversationId}/message")
    public ResponseEntity<AttachmentEntity> uploadMessageAttachment(
            @Valid
            @ValidFile(
                    allowedTypes = {"image/jpeg", "image/png", "application/pdf"},
                    maxSize = 25 * 1024 * 1024,
                    message = "Invalid file(s)"
            )
            @RequestParam("file") MultipartFile file,
            @Valid
            @NotBlank(message = "Conversation id is empty")
            @PathVariable("conversationId") String conversationId,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws IOException {
        AttachmentEntity response = this.messageService.uploadAttachment(
                conversationId,
                file,
                userDetails
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{conversationId}/{key}/message")
    public ResponseEntity<byte[]> downloadMessageAttachment(
            @Valid
            @NotBlank(message = "Conversation id is empty")
            @PathVariable("conversationId") String conversationId,
            @PathVariable("key") String key,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws BadRequestException {
        byte[] data = this.messageService.downloadAttachment(
                conversationId,
                key,
                userDetails
        );
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + key)
                .body(data);
    }
}
