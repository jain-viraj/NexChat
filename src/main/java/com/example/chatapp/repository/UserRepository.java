package com.example.chatapp.repository;

import com.example.chatapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Database operations for User entity.
public interface UserRepository extends JpaRepository<User, Long> {

    // Find a user by username.
    Optional<User> findByUsername(String username);
}
