package com.smartcart.backend.controller;

import com.smartcart.backend.model.Cart;
import com.smartcart.backend.service.CartService;
import com.smartcart.backend.util.JwtUtil;
import com.smartcart.backend.repository.UserRepository;
import com.smartcart.backend.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class CartController {
    
    private final CartService cartService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    
    @GetMapping
    public ResponseEntity<Cart> getUserCart(Authentication authentication) {
        String userId = getUserIdFromAuthentication(authentication);
        log.info("Fetching cart for user: {}", userId);
        
        return cartService.getUserCart(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok(Cart.builder()
                        .userId(userId)
                        .build()));
    }
    
    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(
            Authentication authentication,
            @RequestParam String productId,
            @RequestParam(defaultValue = "1") Integer quantity) {
        
        String userId = getUserIdFromAuthentication(authentication);
        log.info("Adding product {} to cart for user: {} with quantity: {}", productId, userId, quantity);
        
        try {
            Cart updatedCart = cartService.addToCart(userId, productId, quantity);
            return ResponseEntity.ok(updatedCart);
        } catch (RuntimeException e) {
            log.error("Error adding product to cart: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/update")
    public ResponseEntity<Cart> updateCartItemQuantity(
            Authentication authentication,
            @RequestParam String productId,
            @RequestParam Integer quantity) {
        
        String userId = getUserIdFromAuthentication(authentication);
        log.info("Updating quantity for product {} in cart for user: {} to {}", productId, userId, quantity);
        
        try {
            Cart updatedCart = cartService.updateCartItemQuantity(userId, productId, quantity);
            return ResponseEntity.ok(updatedCart);
        } catch (RuntimeException e) {
            log.error("Error updating cart item quantity: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<Cart> removeFromCart(
            Authentication authentication,
            @PathVariable String productId) {
        
        String userId = getUserIdFromAuthentication(authentication);
        log.info("Removing product {} from cart for user: {}", productId, userId);
        
        try {
            Cart updatedCart = cartService.removeFromCart(userId, productId);
            return ResponseEntity.ok(updatedCart);
        } catch (RuntimeException e) {
            log.error("Error removing product from cart: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/clear")
    public ResponseEntity<Map<String, Object>> clearCart(Authentication authentication) {
        String userId = getUserIdFromAuthentication(authentication);
        log.info("Clearing cart for user: {}", userId);
        
        cartService.clearCart(userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Cart cleared successfully");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> getCartItemCount(Authentication authentication) {
        String userId = getUserIdFromAuthentication(authentication);
        
        return cartService.getUserCart(userId)
                .map(cart -> {
                    int itemCount = cart.getItems().stream()
                            .mapToInt(item -> item.getQuantity())
                            .sum();
                    
                    Map<String, Object> response = new HashMap<>();
                    response.put("itemCount", itemCount);
                    response.put("totalItems", cart.getItems().size());
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.ok(Map.of("itemCount", 0, "totalItems", 0)));
    }
    
    private String getUserIdFromAuthentication(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof com.smartcart.backend.model.User) {
            User user = (User) authentication.getPrincipal();
            return user.getId();
        } else if (authentication != null) {
            // Fallback to email-based lookup
            String email = authentication.getName();
            return userRepository.findByEmail(email)
                    .map(User::getId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }
        throw new RuntimeException("User not authenticated");
    }
} 