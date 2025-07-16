package com.example.echo.controllers;

import com.example.echo.requests.MessageRequest;
import com.example.echo.services.MessageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
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

    @MessageMapping("")
    public MessageResponse createNewDMMessage(
            @Valid MessageRequest messageRequest,
            Principal principal
    ) {

        return messageService.createNewDMMessage(
                messageRequest,
                principal
        );
    }

}
