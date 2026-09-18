package com.example.chatapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

// Configures WebSocket + STOMP endpoints and broker.
@Configuration
@EnableWebSocketMessageBroker
@Profile("!cluster")
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // Endpoint for client WebSocket connection.
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")               // Clients connect to /ws
                .setAllowedOriginPatterns("*")    // Allow all origins for demo
                .withSockJS();                    // Use SockJS fallback
    }

    // Configure simple in-memory broker.
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Prefix for server -> client messages (subscriptions).
        registry.enableSimpleBroker("/topic", "/queue");
        // Prefix for client -> server messages (@MessageMapping).
        registry.setApplicationDestinationPrefixes("/app");
        // Prefix for user-specific destinations: /user/queue/...
        registry.setUserDestinationPrefix("/user");
    }
}
