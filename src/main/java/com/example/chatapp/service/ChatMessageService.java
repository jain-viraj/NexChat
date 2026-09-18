package com.example.chatapp.service;

import com.example.chatapp.dto.ChatMessageDto;
import com.example.chatapp.entity.ChatMessage;
import com.example.chatapp.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// Handles saving and loading chat messages.
@Service
public class ChatMessageService {

    private final ChatMessageRepository messageRepository;

    public ChatMessageService(ChatMessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    // Convert DTO -> entity and save to DB.
    public ChatMessage saveMessage(ChatMessageDto dto) {
        ChatMessage msg = new ChatMessage(
                dto.getSender(),
                dto.getReceiver(),
                dto.getContent(),
                LocalDateTime.now()
        );
        return messageRepository.save(msg);
    }

    // Load paginated chat history.
    public Page<ChatMessage> getChatHistory(String userA, String userB, Pageable pageable) {
        return messageRepository.findChatHistory(userA, userB, pageable);
    }
}
