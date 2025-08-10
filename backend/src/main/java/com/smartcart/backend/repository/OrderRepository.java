package com.smartcart.backend.repository;

import com.smartcart.backend.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    
    List<Order> findByUserIdOrderByCreatedAtDesc(String userId);
    
    Page<Order> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);
    
    List<Order> findByStatus(Order.Status status);
    
    @Query("{'createdAt': {$gte: ?0, $lte: ?1}}")
    List<Order> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("{'$and': [{'userId': ?0}, {'status': ?1}]}")
    List<Order> findByUserIdAndStatus(String userId, Order.Status status);
    
    @Query("{'status': {$in: ['PENDING', 'CONFIRMED']}}")
    List<Order> findPendingAndConfirmedOrders();
} 