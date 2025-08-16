package com.smartcart.backend.service;

import com.smartcart.backend.model.Cart;
import com.smartcart.backend.model.CartItem;
import com.smartcart.backend.model.Product;
import com.smartcart.backend.repository.CartRepository;
import com.smartcart.backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {
    
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    
    public Optional<Cart> getUserCart(String userId) {
        return cartRepository.findByUserId(userId);
    }
    
    public Cart addToCart(String userId, String productId, Integer quantity) {
        // Get or create cart for user
        Cart cart = cartRepository.findByUserId(userId)
                .orElse(Cart.builder()
                        .userId(userId)
                        .build());
        
        // Get product details
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) {
            throw new RuntimeException("Product not found");
        }
        
        Product product = productOpt.get();
        
        // Check if product is already in cart
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();
        
        if (existingItem.isPresent()) {
            // Update quantity
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            // Add new item
            CartItem newItem = CartItem.builder()
                    .productId(productId)
                    .quantity(quantity)
                    .productTitle(product.getTitle())
                    .productImageUrl(product.getImageUrl())
                    .productPrice(product.getPrice())
                    .build();
            cart.getItems().add(newItem);
        }
        
        // Calculate total
        updateCartTotal(cart);
        
        return cartRepository.save(cart);
    }
    
    public Cart updateCartItemQuantity(String userId, String productId, Integer quantity) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        
        Optional<CartItem> itemOpt = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();
        
        if (itemOpt.isPresent()) {
            CartItem item = itemOpt.get();
            if (quantity <= 0) {
                cart.getItems().remove(item);
            } else {
                item.setQuantity(quantity);
            }
            updateCartTotal(cart);
            return cartRepository.save(cart);
        } else {
            throw new RuntimeException("Product not found in cart");
        }
    }
    
    public Cart removeFromCart(String userId, String productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        
        cart.getItems().removeIf(item -> item.getProductId().equals(productId));
        updateCartTotal(cart);
        
        return cartRepository.save(cart);
    }
    
    public void clearCart(String userId) {
        cartRepository.findByUserId(userId)
                .ifPresent(cart -> {
                    cart.getItems().clear();
                    updateCartTotal(cart);
                    cartRepository.save(cart);
                });
    }
    
    public void deleteCart(String userId) {
        cartRepository.deleteByUserId(userId);
    }
    
    private void updateCartTotal(Cart cart) {
        BigDecimal total = cart.getItems().stream()
                .map(item -> 
                        item.getProductPrice()
                                .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        cart.setTotal(total);
    }
    
    public boolean isProductInStock(String productId, Integer quantity) {
        Optional<Product> product = productRepository.findById(productId);
        return product.isPresent() && product.get().getStock() >= quantity;
    }
    
    public void updateProductStock(String productId, Integer quantity) {
        productRepository.findById(productId)
                .ifPresent(product -> {
                    product.setStock(product.getStock() - quantity);
                    productRepository.save(product);
                });
    }
} 