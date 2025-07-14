package com.example.goppho.controllers;

import com.example.goppho.entities.MessageEntity;
import com.example.goppho.requests.MessageRequest;
import com.example.goppho.services.MessageService;
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
    public MessageEntity greeting(
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
