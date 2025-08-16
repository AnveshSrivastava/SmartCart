package com.smartcart.backend.controller;

import com.smartcart.backend.dto.AuthRequest;
import com.smartcart.backend.dto.AuthResponse;
import com.smartcart.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AuthController {
    private final AuthService authService;
    
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthRequest request) {
        log.info("Registration attempt for email: {}", request.getEmail());
        AuthResponse response = authService.register(request);
        
        if (response.isSuccess()) {
            log.info("User registered successfully: {}", request.getEmail());
            return ResponseEntity.ok(response);
        } else {
            log.warn("Registration failed for email: {}", request.getEmail());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());
        AuthResponse response = authService.login(request);
        
        if (response.isSuccess()) {
            log.info("User logged in successfully: {}", request.getEmail());
            return ResponseEntity.ok(response);
        } else {
            log.warn("Login failed for email: {}", request.getEmail());
            return ResponseEntity.status(401).body(response);
        }
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String refreshToken = authHeader.substring(7);
            AuthResponse response = authService.refreshToken(refreshToken);
            
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(401).body(response);
            }
        }
        
        AuthResponse errorResponse = AuthResponse.builder()
                .success(false)
                .message("Invalid refresh token")
                .build();
        return ResponseEntity.status(401).body(errorResponse);
    }
    
    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateToken(@RequestHeader("Authorization") String authHeader) {
        Map<String, Object> response = new HashMap<>();
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            // Token validation is handled by the JWT filter
            response.put("valid", true);
            response.put("message", "Token is valid");
            return ResponseEntity.ok(response);
        }
        
        response.put("valid", false);
        response.put("message", "No token provided");
        return ResponseEntity.status(401).body(response);
    }
} 