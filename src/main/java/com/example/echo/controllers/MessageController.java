package com.example.echo.controllers;

import com.example.echo.requests.MessageRequest;
import com.example.echo.responses.MessageResponse;
import com.example.echo.services.MessageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class MessageController {

    MessageService messageService;

    @Autowired
    public MessageController(
            MessageService messageService
    ) {
        this.messageService = messageService;
    }

    @MessageMapping("/message/{conversationId}")
    @SendTo("/topic/{conversationId}")
    public MessageResponse greeting(
            @Valid MessageRequest messageRequest,
            @DestinationVariable String conversationId,
            Principal principal
    ) {

        return messageService.createNewMessage(
                messageRequest,
                conversationId,
                principal.getName()
        );
    }

}
