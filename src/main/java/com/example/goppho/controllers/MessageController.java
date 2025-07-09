package com.example.goppho.controllers;

import com.example.goppho.entities.MessageEntity;
import com.example.goppho.requests.MessageRequest;
import com.example.goppho.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    MessageService messageService;

    @Autowired
    public MessageController(
            MessageService messageService
    ) {
        this.messageService = messageService;
    }

    @MessageMapping("/message/{roomId}")
    @SendTo("/topic/{roomId}")
    public MessageEntity greeting(
            MessageRequest messageRequest,
            @AuthenticationPrincipal UserDetails userDetails,
            @DestinationVariable String conversationId
    ) {
        return messageService.createNewMessage(
                messageRequest,
                conversationId,
                userDetails.getUsername()
        );
    }

}
