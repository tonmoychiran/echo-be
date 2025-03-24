package com.example.goppho.controllers;

import com.example.goppho.entities.MessageEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    @MessageMapping("/message/{roomId}")
    @SendTo("/topic/{roomId}")
    public MessageEntity greeting(
            @DestinationVariable String roomId,
            MessageEntity message
    ) {
        System.out.println(message.getMessage());
        return message;
    }

}
