package online.threadly.product.service;

import online.threadly.product.model.Product;
import online.threadly.product.repository.ProductRepository;
import online.threadly.product.exception.BadRequestException;
import online.threadly.product.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product createProduct(Product product) {
        if (product == null) {
            throw new BadRequestException("product payload is required");
        }
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new BadRequestException("product name is required");
        }
        if (product.getPrice() == null || product.getPrice() < 0) {
            throw new BadRequestException("product price must be >= 0");
        }
        if (product.getStock() != null && product.getStock() < 0) {
            throw new BadRequestException("product stock must be >= 0");
        }
        return productRepository.save(product);
    }

    public List<Product> getProducts() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("products not found");
        }
        return products;
    }

    // SELECT * FROM products WHERE id = id;
    public Product getProduct(UUID id) {
        // Optional<Product> product = productRepository.findById(id);
        // if(product.isPresent()) {
        // return product.get();
        // }
        // return null;
        if (id == null) {
            throw new BadRequestException("product id is required");
        }
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("product not found"));
    }

    // SELECT * FROM products WHERE slug = 'men-slim-fit-smart-formal-shirt';
    public Product getProductBySlug(String slug) {
        if (slug == null || slug.trim().isEmpty()) {
            throw new BadRequestException("product slug is required");
        }
        return productRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("product not found"));
    }

    public List<Product> getProductsByIds(List<UUID> productIds) {
        if(productIds.isEmpty()) {
            throw new BadRequestException("productids cannot be empty");
        }
        return productRepository.findAllById(productIds);
    }

}
