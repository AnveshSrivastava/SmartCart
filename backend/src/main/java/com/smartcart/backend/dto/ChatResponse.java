package com.smartcart.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {
    
    private String reply;
    private List<ChatProduct> products;
    private String conversationId;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatProduct {
        private String title;
        private Double price;
        private String imageUrl;
        private String id;
        private String category;
    }
}

