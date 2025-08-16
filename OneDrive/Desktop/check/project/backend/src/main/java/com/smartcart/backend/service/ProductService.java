package com.smartcart.backend.service;

import com.smartcart.backend.dto.ProductDTO;
import com.smartcart.backend.model.Product;
import com.smartcart.backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    
    private final ProductRepository productRepository;
    
    public List<Product> getAllProducts() {
        return productRepository.findByActiveTrue();
    }
    
    public Page<Product> getAllProductsPaginated(Pageable pageable) {
        return productRepository.findByActiveTrue(pageable);
    }
    
    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }
    
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }
    
    public List<Product> searchProducts(String searchTerm) {
        return productRepository.searchProducts(searchTerm);
    }
    
    public List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByPriceRange(minPrice, maxPrice);
    }
    
    public List<Product> getProductsByCategoryAndPriceRange(String category, BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByCategoryAndPriceRange(category, minPrice, maxPrice);
    }
    
    public List<Product> getAvailableProducts() {
        return productRepository.findAvailableProducts();
    }
    
    public List<Product> getProductsByRating(Double minRating) {
        return productRepository.findByRatingGreaterThanEqual(minRating);
    }
    
    public Product createProduct(ProductDTO productDTO) {
        Product product = Product.builder()
                .title(productDTO.getTitle())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .category(productDTO.getCategory())
                .stock(productDTO.getStock())
                .imageUrl(productDTO.getImageUrl())
                .rating(productDTO.getRating() != null ? productDTO.getRating() : 0.0)
                .reviewCount(productDTO.getReviewCount() != null ? productDTO.getReviewCount() : 0)
                .active(productDTO.isActive())
                .build();
        
        return productRepository.save(product);
    }
    
    public Optional<Product> updateProduct(String id, ProductDTO productDTO) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setTitle(productDTO.getTitle());
                    existingProduct.setDescription(productDTO.getDescription());
                    existingProduct.setPrice(productDTO.getPrice());
                    existingProduct.setCategory(productDTO.getCategory());
                    existingProduct.setStock(productDTO.getStock());
                    existingProduct.setImageUrl(productDTO.getImageUrl());
                    existingProduct.setActive(productDTO.isActive());
                    
                    if (productDTO.getRating() != null) {
                        existingProduct.setRating(productDTO.getRating());
                    }
                    if (productDTO.getReviewCount() != null) {
                        existingProduct.setReviewCount(productDTO.getReviewCount());
                    }
                    
                    return productRepository.save(existingProduct);
                });
    }
    
    public boolean deleteProduct(String id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            Product existingProduct = product.get();
            existingProduct.setActive(false);
            productRepository.save(existingProduct);
            return true;
        }
        return false;
    }
    
    public boolean updateProductStock(String id, Integer newStock) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setStock(newStock);
                    productRepository.save(product);
                    return true;
                })
                .orElse(false);
    }
    
    public boolean updateProductRating(String id, Double rating, Integer reviewCount) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setRating(rating);
                    product.setReviewCount(reviewCount);
                    productRepository.save(product);
                    return true;
                })
                .orElse(false);
    }
} 