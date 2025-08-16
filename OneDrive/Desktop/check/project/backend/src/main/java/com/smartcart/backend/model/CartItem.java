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
@Document(collection = "cart_items")
public class CartItem {
    
    @Id
    private String id;
    
    @NotBlank(message = "Product ID is required")
    private String productId;
    
    @Positive(message = "Quantity must be positive")
    private Integer quantity;
    
    private String productTitle;
    private String productImageUrl;
    private BigDecimal productPrice;
} 