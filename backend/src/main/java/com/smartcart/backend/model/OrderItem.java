package com.smartcart.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "order_items")
public class OrderItem {
    
    @Id
    private String id;
    
    @NotBlank(message = "Product ID is required")
    private String productId;
    
    @NotBlank(message = "Product title is required")
    private String productTitle;
    
    private String productImageUrl;
    
    @Positive(message = "Quantity must be positive")
    private Integer quantity;
    
    @Positive(message = "Price must be positive")
    private BigDecimal price;
    
    @Positive(message = "Total must be positive")
    private BigDecimal total;
} 