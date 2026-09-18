package com.example.chatapp.controller;

import com.example.chatapp.dto.ChatMessageDto;
import com.example.chatapp.entity.ChatMessage;
import com.example.chatapp.service.ChatMessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

// Handles incoming WebSocket chat messages.
@Controller
public class ChatWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

    public ChatWebSocketController(SimpMessagingTemplate messagingTemplate,
                                   ChatMessageService chatMessageService) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageService = chatMessageService;
    }

    // Client sends to /app/chat.send with a ChatMessageDto JSON.
    @MessageMapping("/chat.send")
    public void sendMessage(ChatMessageDto message) {
        // 1. Save message to database.
        ChatMessage saved = chatMessageService.saveMessage(message);

        // 2. Build DTO with timestamp to send to clients.
        ChatMessageDto payload = new ChatMessageDto(
                saved.getSenderUsername(),
                saved.getReceiverUsername(),
                saved.getContent(),
                saved.getTimestamp()
        );

        // 3. Send to receiver's explicit queue.
        messagingTemplate.convertAndSend(
                "/queue/messages/" + message.getReceiver(),
                payload
        );

        // 4. Also send to sender (to update their own chat window).
        if (!message.getSender().equals(message.getReceiver())) {
            messagingTemplate.convertAndSend(
                    "/queue/messages/" + message.getSender(),
                    payload
            );
        }
    }
}
