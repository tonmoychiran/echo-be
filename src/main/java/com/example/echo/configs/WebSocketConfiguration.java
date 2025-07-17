package com.example.echo.configs;

import com.example.echo.services.UserService;
import com.example.echo.services.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    private final UserService userService; // Example service to inject


    @Autowired
    public WebSocketConfiguration(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/inbox");
        config.setApplicationDestinationPrefixes("/outbox");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOrigins("*");
    }

    @EventListener
    public void handleSessionConnect(SessionConnectedEvent event) {
        this.userService.handleWebSocketSessionConnect(event.getUser());
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        this.userService.handleWebSocketSessionDisconnect(event.getUser());
    }
}
