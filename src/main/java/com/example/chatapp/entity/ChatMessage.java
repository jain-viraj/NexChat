package com.example.chatapp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Represents a single chat message between two users.
@Entity
@Table(name = "messages", indexes = {
        @Index(name = "idx_sender", columnList = "senderUsername"),
        @Index(name = "idx_receiver", columnList = "receiverUsername")
})
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Username of the sender.
    @Column(nullable = false)
    private String senderUsername;

    // Username of the receiver.
    @Column(nullable = false)
    private String receiverUsername;

    // Text content of the message.
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // When the message was created.
    @Column(nullable = false)
    private LocalDateTime timestamp;

    public ChatMessage() {
    }

    public ChatMessage(String senderUsername,
                       String receiverUsername,
                       String content,
                       LocalDateTime timestamp) {
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        this.content = content;
        this.timestamp = timestamp;
    }

    // Getters and setters.

    public Long getId() {
        return id;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
