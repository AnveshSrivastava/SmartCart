package com.smartcart.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    
    private String id;
    
    @NotBlank(message = "Product title is required")
    @Size(min = 2, max = 200, message = "Title must be between 2 and 200 characters")
    private String title;
    
    @NotBlank(message = "Product description is required")
    @Size(min = 10, max = 2000, message = "Description must be between 10 and 2000 characters")
    private String description;
    
    @NotNull(message = "Product price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;
    
    @NotBlank(message = "Product category is required")
    @Size(min = 2, max = 100, message = "Category must be between 2 and 100 characters")
    private String category;
    
    private Double rating;
    private Integer reviewCount;
    
    @NotNull(message = "Product stock is required")
    @jakarta.validation.constraints.PositiveOrZero(message = "Stock must be zero or positive")
    private Integer stock;
    
    private String imageUrl;
    private boolean active;
} 