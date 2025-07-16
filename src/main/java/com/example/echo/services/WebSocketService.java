package com.example.echo.services;

import com.example.echo.interfaces.Publishable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketService(
            SimpMessagingTemplate messagingTemplate
    ){
        this.messagingTemplate = messagingTemplate;
    }

    protected void publishToUser(String userId, Publishable payload){
        messagingTemplate.convertAndSend("/outbox/" + userId, payload);
    }
}
