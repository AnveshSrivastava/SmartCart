package com.smartcart.backend.repository;

import com.smartcart.backend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    
    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    @Query("{'role': ?0}")
    List<User> findByRole(User.Role role);
    
    @Query("{'active': true}")
    List<User> findActiveUsers();
} 