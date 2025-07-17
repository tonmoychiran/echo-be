package com.example.echo.controllers;

import com.example.echo.entities.ConversationEntity;
import com.example.echo.responses.GetResponse;
import com.example.echo.services.ConversationService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/conversation")
public class ConversationController {
    ConversationService conversationService;

    @Autowired
    public ConversationController(
            ConversationService conversationService
    ) {
        this.conversationService = conversationService;
    }

    @GetMapping("")
    public ResponseEntity<GetResponse<ConversationEntity>> getConversation(
            @NotBlank(message = "Friend id is empty")
            @RequestParam String friendId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        GetResponse<ConversationEntity> response = this.conversationService.getPrivateConversation(
                userDetails,
                friendId
        );

        return ResponseEntity.ok(
                response
        );
    }
}
