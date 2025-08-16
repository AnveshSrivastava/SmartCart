package com.smartcart.backend.service;

import com.smartcart.backend.model.Cart;
import com.smartcart.backend.model.CartItem;
import com.smartcart.backend.model.Order;
import com.smartcart.backend.model.OrderItem;
import com.smartcart.backend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final CartService cartService;
    
    public Order placeOrder(String userId, String shippingAddress, String paymentInfo) {
        // Get user's cart
        Optional<Cart> cartOpt = cartService.getUserCart(userId);
        if (cartOpt.isEmpty() || cartOpt.get().getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }
        
        Cart cart = cartOpt.get();
        
        // Validate stock for all items
        for (CartItem item : cart.getItems()) {
            if (!cartService.isProductInStock(item.getProductId(), item.getQuantity())) {
                throw new RuntimeException("Insufficient stock for product: " + item.getProductTitle());
            }
        }
        
        // Create order items from cart items
        List<OrderItem> orderItems = cart.getItems().stream()
                .map(this::convertCartItemToOrderItem)
                .collect(Collectors.toList());
        
        // Create order
        Order order = Order.builder()
                .userId(userId)
                .items(orderItems)
                .total(cart.getTotal())
                .shippingAddress(shippingAddress)
                .paymentInfo(paymentInfo)
                .status(Order.Status.PENDING)
                .build();
        
        Order savedOrder = orderRepository.save(order);
        
        // Update product stock
        for (CartItem item : cart.getItems()) {
            cartService.updateProductStock(item.getProductId(), item.getQuantity());
        }
        
        // Clear user's cart
        cartService.clearCart(userId);
        
        return savedOrder;
    }
    
    public List<Order> getUserOrders(String userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
    
    public Page<Order> getUserOrdersPaginated(String userId, Pageable pageable) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }
    
    public Optional<Order> getOrderById(String orderId) {
        return orderRepository.findById(orderId);
    }
    
    public List<Order> getOrdersByStatus(Order.Status status) {
        return orderRepository.findByStatus(status);
    }
    
    public List<Order> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByDateRange(startDate, endDate);
    }
    
    public List<Order> getUserOrdersByStatus(String userId, Order.Status status) {
        return orderRepository.findByUserIdAndStatus(userId, status);
    }
    
    public Optional<Order> updateOrderStatus(String orderId, Order.Status status) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    order.setStatus(status);
                    return orderRepository.save(order);
                });
    }
    
    public Optional<Order> updateOrderTracking(String orderId, String trackingNumber) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    order.setTrackingNumber(trackingNumber);
                    return orderRepository.save(order);
                });
    }
    
    public List<Order> getPendingAndConfirmedOrders() {
        return orderRepository.findPendingAndConfirmedOrders();
    }
    
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    private OrderItem convertCartItemToOrderItem(CartItem cartItem) {
        return OrderItem.builder()
                .productId(cartItem.getProductId())
                .productTitle(cartItem.getProductTitle())
                .productImageUrl(cartItem.getProductImageUrl())
                .quantity(cartItem.getQuantity())
                .price(cartItem.getProductPrice())
                .total(cartItem.getProductPrice()
                        .multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .build();
    }
} 