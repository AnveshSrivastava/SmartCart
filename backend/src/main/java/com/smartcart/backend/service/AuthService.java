package com.smartcart.backend.service;

import com.smartcart.backend.dto.AuthRequest;
import com.smartcart.backend.dto.AuthResponse;
import com.smartcart.backend.model.User;
import com.smartcart.backend.repository.UserRepository;
import com.smartcart.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    @Lazy
    private final AuthenticationManager authenticationManager;
    

    
    public AuthResponse register(AuthRequest request) {
        try {
            // Check if user already exists
            if (userRepository.existsByEmail(request.getEmail())) {
                return AuthResponse.builder()
                        .success(false)
                        .message("User with this email already exists")
                        .build();
            }
            
            // Create new user
            User user = User.builder()
                    .fullName(request.getFullName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(User.Role.USER)
                    .build();
            
            User savedUser = userRepository.save(user);
            
            // Generate tokens
            String token = jwtUtil.generateToken(user, savedUser.getId(), savedUser.getRole().name());
            String refreshToken = jwtUtil.generateRefreshToken(user);
            
            return AuthResponse.builder()
                    .success(true)
                    .token(token)
                    .refreshToken(refreshToken)
                    .userId(savedUser.getId())
                    .email(savedUser.getEmail())
                    .fullName(savedUser.getFullName())
                    .role(savedUser.getRole().name())
                    .message("User registered successfully")
                    .build();
                    
        } catch (Exception e) {
            log.error("Error during user registration: {}", e.getMessage());
            return AuthResponse.builder()
                    .success(false)
                    .message("Registration failed: " + e.getMessage())
                    .build();
        }
    }
    
    public AuthResponse login(AuthRequest request) {
        try {
            // Authenticate user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            
            // Load user details
            User user = (User) userDetailsService.loadUserByUsername(request.getEmail());
            
            // Generate tokens
            String token = jwtUtil.generateToken(user, user.getId(), user.getRole().name());
            String refreshToken = jwtUtil.generateRefreshToken(user);
            
            return AuthResponse.builder()
                    .success(true)
                    .token(token)
                    .refreshToken(refreshToken)
                    .userId(user.getId())
                    .email(user.getEmail())
                    .fullName(user.getFullName())
                    .role(user.getRole().name())
                    .message("Login successful")
                    .build();
                    
        } catch (Exception e) {
            log.error("Error during login: {}", e.getMessage());
            return AuthResponse.builder()
                    .success(false)
                    .message("Login failed: Invalid credentials")
                    .build();
        }
    }
    
    public AuthResponse refreshToken(String refreshToken) {
        try {
            if (jwtUtil.isTokenValid(refreshToken)) {
                String email = jwtUtil.extractUsername(refreshToken);
                User user = (User) userDetailsService.loadUserByUsername(email);
                
                String newToken = jwtUtil.generateToken(user, user.getId(), user.getRole().name());
                String newRefreshToken = jwtUtil.generateRefreshToken(user);
                
                return AuthResponse.builder()
                        .success(true)
                        .token(newToken)
                        .refreshToken(newRefreshToken)
                        .userId(user.getId())
                        .email(user.getEmail())
                        .fullName(user.getFullName())
                        .role(user.getRole().name())
                        .message("Token refreshed successfully")
                        .build();
            } else {
                return AuthResponse.builder()
                        .success(false)
                        .message("Invalid refresh token")
                        .build();
            }
        } catch (Exception e) {
            log.error("Error refreshing token: {}", e.getMessage());
            return AuthResponse.builder()
                    .success(false)
                    .message("Token refresh failed")
                    .build();
        }
    }
} 