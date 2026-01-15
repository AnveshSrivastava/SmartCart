package online.threadly.product.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String slug;

    private String[] images;

    private String brand;

    private String description;

    private Integer stock;

    private Double price;

    private Double rating;

    private Integer ratingCount;

    private Boolean isFeatured;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        System.out.println("Hello, I am printing my name because I want to do it before saving a new product to the database.");
        this.createdAt = LocalDateTime.now();
    }
}
