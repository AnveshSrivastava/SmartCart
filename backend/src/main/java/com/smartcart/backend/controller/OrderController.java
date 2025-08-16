package com.smartcart.backend.controller;

import com.smartcart.backend.model.Order;
import com.smartcart.backend.service.OrderService;
import com.smartcart.backend.repository.UserRepository;
import com.smartcart.backend.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class OrderController {
    
    private final OrderService orderService;
    private final UserRepository userRepository;
    
    @PostMapping("/place")
    public ResponseEntity<Order> placeOrder(
            Authentication authentication,
            @RequestParam String shippingAddress,
            @RequestParam(required = false) String paymentInfo) {
        
        String userId = getUserIdFromAuthentication(authentication);
        log.info("Placing order for user: {}", userId);
        
        try {
            Order order = orderService.placeOrder(userId, shippingAddress, paymentInfo);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            log.error("Error placing order: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/history")
    public ResponseEntity<List<Order>> getUserOrders(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        String userId = getUserIdFromAuthentication(authentication);
        log.info("Fetching order history for user: {}", userId);
        
        if (page == 0 && size == 10) {
            // Return all orders without pagination
            List<Order> orders = orderService.getUserOrders(userId);
            return ResponseEntity.ok(orders);
        } else {
            // Return paginated orders
            Pageable pageable = PageRequest.of(page, size);
            Page<Order> orderPage = orderService.getUserOrdersPaginated(userId, pageable);
            return ResponseEntity.ok(orderPage.getContent());
        }
    }
    
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(
            Authentication authentication,
            @PathVariable String orderId) {
        
        String userId = getUserIdFromAuthentication(authentication);
        log.info("Fetching order {} for user: {}", orderId, userId);
        
        Optional<Order> order = orderService.getOrderById(orderId);
        if (order.isPresent() && order.get().getUserId().equals(userId)) {
            return ResponseEntity.ok(order.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Order>> getUserOrdersByStatus(
            Authentication authentication,
            @PathVariable Order.Status status) {
        
        String userId = getUserIdFromAuthentication(authentication);
        log.info("Fetching orders with status {} for user: {}", status, userId);
        
        List<Order> orders = orderService.getUserOrdersByStatus(userId, status);
        return ResponseEntity.ok(orders);
    }
    
    // Admin endpoints
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Order>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("Admin fetching all orders");
        // This would need to be implemented in OrderService
        return ResponseEntity.ok(List.of());
    }
    
    @GetMapping("/admin/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable Order.Status status) {
        log.info("Admin fetching orders with status: {}", status);
        List<Order> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }
    
    @PutMapping("/admin/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable String orderId,
            @RequestParam Order.Status status) {
        
        log.info("Admin updating order {} status to {}", orderId, status);
        Optional<Order> updatedOrder = orderService.updateOrderStatus(orderId, status);
        return updatedOrder.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/admin/{orderId}/tracking")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Order> updateOrderTracking(
            @PathVariable String orderId,
            @RequestParam String trackingNumber) {
        
        log.info("Admin updating tracking number for order {} to {}", orderId, trackingNumber);
        Optional<Order> updatedOrder = orderService.updateOrderTracking(orderId, trackingNumber);
        return updatedOrder.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/admin/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Order>> getPendingAndConfirmedOrders() {
        log.info("Admin fetching pending and confirmed orders");
        List<Order> orders = orderService.getPendingAndConfirmedOrders();
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/admin/date-range")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Order>> getOrdersByDateRange(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        
        log.info("Admin fetching orders between {} and {}", startDate, endDate);
        List<Order> orders = orderService.getOrdersByDateRange(startDate, endDate);
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/admin/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getOrderStats() {
        log.info("Fetching order statistics");
        List<Order> allOrders = orderService.getAllOrders();
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalOrders", allOrders.size());
        stats.put("pendingOrders", allOrders.stream().filter(o -> o.getStatus() == Order.Status.PENDING).count());
        stats.put("confirmedOrders", allOrders.stream().filter(o -> o.getStatus() == Order.Status.CONFIRMED).count());
        stats.put("shippedOrders", allOrders.stream().filter(o -> o.getStatus() == Order.Status.SHIPPED).count());
        stats.put("deliveredOrders", allOrders.stream().filter(o -> o.getStatus() == Order.Status.DELIVERED).count());
        stats.put("totalSales", allOrders.stream().mapToDouble(o -> o.getTotal().doubleValue()).sum());
        
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getOrderStats(Authentication authentication) {
        String userId = getUserIdFromAuthentication(authentication);
        log.info("Fetching order stats for user: {}", userId);
        
        List<Order> userOrders = orderService.getUserOrders(userId);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalOrders", userOrders.size());
        stats.put("pendingOrders", userOrders.stream()
                .filter(order -> order.getStatus() == Order.Status.PENDING)
                .count());
        stats.put("deliveredOrders", userOrders.stream()
                .filter(order -> order.getStatus() == Order.Status.DELIVERED)
                .count());
        stats.put("totalSpent", userOrders.stream()
                .mapToDouble(order -> order.getTotal().doubleValue())
                .sum());
        
        return ResponseEntity.ok(stats);
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