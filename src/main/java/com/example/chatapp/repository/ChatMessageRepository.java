package com.example.chatapp.repository;

import com.example.chatapp.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// Database operations for ChatMessage entity.
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // Load chat history between two users (paginated, ordered newest first)
    @Query("""
        SELECT m FROM ChatMessage m
        WHERE (m.senderUsername = :userA AND m.receiverUsername = :userB)
           OR (m.senderUsername = :userB AND m.receiverUsername = :userA)
        ORDER BY m.timestamp DESC
    """)
    Page<ChatMessage> findChatHistory(
            @Param("userA") String userA,
            @Param("userB") String userB,
            Pageable pageable);
}
