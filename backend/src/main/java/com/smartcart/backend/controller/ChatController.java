package com.smartcart.backend.controller;

import com.smartcart.backend.dto.ChatRequest;
import com.smartcart.backend.dto.ChatResponse;
import com.smartcart.backend.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ChatController {
    
    private final ChatService chatService;
    
    @PostMapping
    public ResponseEntity<ChatResponse> sendMessage(@Valid @RequestBody ChatRequest request) {
        try {
            log.info("Received chat message: conversationId={}, message={}", 
                request.getConversationId(), request.getMessage());
            
            ChatResponse response = chatService.processMessage(request);
            
            log.info("Generated chat response: conversationId={}, productsCount={}", 
                response.getConversationId(), 
                response.getProducts() != null ? response.getProducts().size() : 0);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error processing chat message", e);
            
            // Return a fallback response
            ChatResponse fallbackResponse = ChatResponse.builder()
                .conversationId(request.getConversationId())
                .reply("I'm sorry, I'm having trouble processing your request right now. Please try again later.")
                .products(java.util.Collections.emptyList())
                .build();
            
            return ResponseEntity.ok(fallbackResponse);
        }
    }
}

