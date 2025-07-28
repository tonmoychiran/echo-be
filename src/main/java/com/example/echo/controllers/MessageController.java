package com.example.echo.controllers;


import com.example.echo.entities.MessageEntity;
import com.example.echo.requests.MessageRequest;
import com.example.echo.responses.GetResponse;
import com.example.echo.services.MessageService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/message")
public class MessageController {

    MessageService messageService;

    @Autowired
    public MessageController(
            MessageService messageService
    ) {
        this.messageService = messageService;
    }

    @PostMapping("/{conversationId}")
    public ResponseEntity<MessageEntity> createNewMessage(
            @PathVariable("conversationId") String conversationId,
            @Valid @RequestBody MessageRequest messageRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws BadRequestException {
        System.out.println('c'+40);
        MessageEntity response = messageService.createNewMessage(
                conversationId,
                messageRequest,
                userDetails
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{conversationId}")
    public ResponseEntity<GetResponse<List<MessageEntity>>> getAllMessages(
            @PathVariable("conversationId") String conversationId,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Optional<Long> before
    ) throws BadRequestException {
        GetResponse<List<MessageEntity>> response = this.messageService.getMessage(
                conversationId,
                userDetails,
                before
        );

        return ResponseEntity.ok(response);
    }

}
