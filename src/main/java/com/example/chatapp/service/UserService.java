package com.example.chatapp.service;

import com.example.chatapp.entity.User;
import com.example.chatapp.repository.UserRepository;
import org.springframework.stereotype.Service;

// Handles simple user operations like register/login.
@Service
public class UserService {

    private final UserRepository userRepository;

    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    // Constructor injection.
    public UserService(UserRepository userRepository, org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Register a new user with BCrypt hashed password
    public User register(String username, String rawPassword) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        User user = new User(username, passwordEncoder.encode(rawPassword));
        return userRepository.save(user);
    }

    // Get all registered users.
    public java.util.List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
