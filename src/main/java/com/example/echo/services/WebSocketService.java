package com.example.echo.services;

import com.example.echo.entities.UserEntity;
import com.example.echo.interfaces.Publishable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketService(
            SimpMessagingTemplate messagingTemplate
    ) {
        this.messagingTemplate = messagingTemplate;
    }

    protected void publishToUserList(List<UserEntity> userList, Publishable payload) {
        userList.forEach(
                user -> {
                    messagingTemplate.convertAndSend("/inbox/" + user.getUserId(), payload);
                }
        );
    }
}
