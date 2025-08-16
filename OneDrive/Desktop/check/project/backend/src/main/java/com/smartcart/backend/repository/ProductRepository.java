package com.smartcart.backend.repository;

import com.smartcart.backend.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    
    List<Product> findByCategory(String category);
    
    List<Product> findByActiveTrue();
    
    Page<Product> findByActiveTrue(Pageable pageable);
    
    @Query("{'$and': [{'active': true}, {'$or': [{'title': {$regex: ?0, $options: 'i'}}, {'description': {$regex: ?0, $options: 'i'}}]}]}")
    List<Product> searchProducts(String searchTerm);
    
    @Query("{'$and': [{'active': true}, {'price': {$gte: ?0, $lte: ?1}}]}")
    List<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    
    @Query("{'$and': [{'active': true}, {'category': ?0}, {'price': {$gte: ?1, $lte: ?2}}]}")
    List<Product> findByCategoryAndPriceRange(String category, BigDecimal minPrice, BigDecimal maxPrice);
    
    @Query("{'$and': [{'active': true}, {'stock': {$gt: 0}}]}")
    List<Product> findAvailableProducts();
    
    @Query("{'$and': [{'active': true}, {'rating': {$gte: ?0}}]}")
    List<Product> findByRatingGreaterThanEqual(Double rating);
} 