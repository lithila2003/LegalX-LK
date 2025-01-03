package com.example.sdgp.Service;

import com.example.sdgp.dto.LoginRequest;
import com.example.sdgp.dto.RegisterRequest;
import com.example.sdgp.exception.CustomException;
import com.example.sdgp.model.User;
import com.example.sdgp.Repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ResponseEntity<String> register(RegisterRequest request) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new CustomException("Username is already taken");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException("Email is already registered");
        }

        // Create and save the new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        // Respond with a plain text message
        return ResponseEntity.ok("User registered successfully");
    }

    public ResponseEntity<String> login(LoginRequest request) {
        // Find user by username
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new CustomException("User not found"));

        // Validate password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException("Invalid password");
        }

        // Respond with a plain text message
        return ResponseEntity.ok("Login successful");
    }
}

