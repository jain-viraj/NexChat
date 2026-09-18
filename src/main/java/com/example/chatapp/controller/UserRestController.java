package com.example.chatapp.controller;

import com.example.chatapp.dto.ChatMessageDto;
import com.example.chatapp.entity.ChatMessage;
import com.example.chatapp.entity.User;
import com.example.chatapp.service.ChatMessageService;
import com.example.chatapp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.example.chatapp.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

// Exposes endpoints for authentication and loading chat history.
@RestController
@RequestMapping("/api")
public class UserRestController {

    private final UserService userService;
    private final ChatMessageService chatMessageService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public UserRestController(UserService userService,
                              ChatMessageService chatMessageService,
                              AuthenticationManager authenticationManager,
                              JwtUtil jwtUtil,
                              UserDetailsService userDetailsService) {
        this.userService = userService;
        this.chatMessageService = chatMessageService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    // Explicit DTOs for Auth
    public static class AuthRequest {
        public String username;
        public String password;
    }

    public static class AuthResponse {
        public String token;
        public String username;
        public AuthResponse(String token, String username) {
            this.token = token;
            this.username = username;
        }
    }

    // Register a new user with password
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        try {
            userService.register(request.username, request.password);
            return ResponseEntity.ok("User registered successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Login and get JWT
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username, request.password)
            );
            
            final UserDetails userDetails = userDetailsService.loadUserByUsername(request.username);
            final String jwt = jwtUtil.generateToken(userDetails.getUsername());
            
            return ResponseEntity.ok(new AuthResponse(jwt, userDetails.getUsername()));
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    // Get all registered users.
    @GetMapping("/users")
    public ResponseEntity<List<String>> getAllUsers() {
        List<String> users = userService.getAllUsers().stream()
                .map(User::getUsername)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    // Return paginated chat history between two users.
    @GetMapping("/history")
    public ResponseEntity<Map<String, Object>> history(
            @RequestParam("user") String user,
            @RequestParam("partner") String partner,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ChatMessage> messagePage = chatMessageService.getChatHistory(user, partner, pageable);

        // Map to DTOs
        List<ChatMessageDto> dtoList = messagePage.getContent().stream()
                .map(m -> new ChatMessageDto(
                        m.getSenderUsername(),
                        m.getReceiverUsername(),
                        m.getContent(),
                        m.getTimestamp()
                ))
                .collect(Collectors.toList());

        // Reverse the list so the oldest is first for the UI render
        Collections.reverse(dtoList);

        Map<String, Object> response = new HashMap<>();
        response.put("messages", dtoList);
        response.put("currentPage", messagePage.getNumber());
        response.put("totalItems", messagePage.getTotalElements());
        response.put("totalPages", messagePage.getTotalPages());
        
        return ResponseEntity.ok(response);
    }
}
