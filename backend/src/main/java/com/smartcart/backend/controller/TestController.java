package com.smartcart.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {
    
    @GetMapping("/health")
    public Map<String, Object> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "SmartCart Backend is running successfully!");
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
    
    @GetMapping("/products")
    public Map<String, Object> getTestProducts() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Test products endpoint working!");
        response.put("data", "Products would be loaded from database");
        return response;
    }
} 