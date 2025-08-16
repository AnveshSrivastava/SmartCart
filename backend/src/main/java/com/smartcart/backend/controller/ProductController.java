package com.smartcart.backend.controller;

import com.smartcart.backend.dto.ProductDTO;
import com.smartcart.backend.model.Product;
import com.smartcart.backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ProductController {
    
    private final ProductService productService;
    
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        if (page == 0 && size == 10) {
            // Return all products without pagination
            List<Product> products = productService.getAllProducts();
            return ResponseEntity.ok(products);
        } else {
            // Return paginated products
            Pageable pageable = PageRequest.of(page, size);
            Page<Product> productPage = productService.getAllProductsPaginated(pageable);
            return ResponseEntity.ok(productPage.getContent());
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        List<Product> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String q) {
        List<Product> products = productService.searchProducts(q);
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/price-range")
    public ResponseEntity<List<Product>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        List<Product> products = productService.getProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/category/{category}/price-range")
    public ResponseEntity<List<Product>> getProductsByCategoryAndPriceRange(
            @PathVariable String category,
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        List<Product> products = productService.getProductsByCategoryAndPriceRange(category, minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/available")
    public ResponseEntity<List<Product>> getAvailableProducts() {
        List<Product> products = productService.getAvailableProducts();
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/rating/{minRating}")
    public ResponseEntity<List<Product>> getProductsByRating(@PathVariable Double minRating) {
        List<Product> products = productService.getProductsByRating(minRating);
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/admin/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getProductStats() {
        log.info("Fetching product statistics");
        List<Product> allProducts = productService.getAllProducts();
        List<Product> lowStockProducts = allProducts.stream()
                .filter(product -> product.getStock() < 5)
                .toList();
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalProducts", allProducts.size());
        stats.put("activeProducts", allProducts.stream().filter(Product::isActive).count());
        stats.put("lowStockProducts", lowStockProducts.size());
        stats.put("outOfStockProducts", allProducts.stream().filter(p -> p.getStock() == 0).count());
        
        return ResponseEntity.ok(stats);
    }
    
    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        log.info("Creating new product: {}", productDTO.getTitle());
        Product createdProduct = productService.createProduct(productDTO);
        return ResponseEntity.ok(createdProduct);
    }
    
    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> updateProduct(@PathVariable String id, @Valid @RequestBody ProductDTO productDTO) {
        log.info("Updating product with id: {}", id);
        Optional<Product> updatedProduct = productService.updateProduct(id, productDTO);
        return updatedProduct.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable String id) {
        log.info("Deleting product with id: {}", id);
        boolean deleted = productService.deleteProduct(id);
        
        Map<String, Object> response = new HashMap<>();
        if (deleted) {
            response.put("success", true);
            response.put("message", "Product deleted successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Product not found");
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/admin/{id}/stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> updateProductStock(
            @PathVariable String id,
            @RequestParam Integer stock) {
        log.info("Updating stock for product with id: {} to {}", id, stock);
        boolean updated = productService.updateProductStock(id, stock);
        
        Map<String, Object> response = new HashMap<>();
        if (updated) {
            response.put("success", true);
            response.put("message", "Stock updated successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Product not found");
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/admin/{id}/rating")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> updateProductRating(
            @PathVariable String id,
            @RequestParam Double rating,
            @RequestParam Integer reviewCount) {
        log.info("Updating rating for product with id: {} to {}", id, rating);
        boolean updated = productService.updateProductRating(id, rating, reviewCount);
        
        Map<String, Object> response = new HashMap<>();
        if (updated) {
            response.put("success", true);
            response.put("message", "Rating updated successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Product not found");
            return ResponseEntity.notFound().build();
        }
    }
} 