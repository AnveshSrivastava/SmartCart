package online.threadly.product.controller;

import online.threadly.product.dao.Response;
import online.threadly.product.model.Product;
import online.threadly.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class ProductController {

    @Autowired
    private ProductService productService;

    // this needs to be converted to a ADMIN API
    @PostMapping("/admin/products")
    public ResponseEntity<Response> createProduct(@RequestBody Product product) {
        var createdProduct = productService.createProduct(product);
        if (createdProduct == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("product not created", null));
        }
        return ResponseEntity.ok(new Response("product created successfully", createdProduct));
    }

    @GetMapping("/products")
    public ResponseEntity<Response> getProducts() {
        var products = productService.getProducts();
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("products not found", null));
        }
        return ResponseEntity.ok(new Response("products fetched successfully", products));
    }

    // http://localhost:8082/api/v1/products/slug/men-slim-fit-smart-formal-shirt
    @GetMapping("/products/slug/{slug}")
    public ResponseEntity<Response> getProductBySlug(@PathVariable String slug) {
        Product product = productService.getProductBySlug(slug);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("product not found", null));
        }
        return ResponseEntity.ok(new Response("product fetched successfully", product));
    }

    // http://localhost:8082/api/v1/products/ea1e7f2f-409d-4681-b3e9-d80fd3a7b042
    @GetMapping("/products/{id}")
    public ResponseEntity<Response> getProduct(@PathVariable UUID id) {
        Product product = productService.getProduct(id);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("product not found", null));
        }
        return ResponseEntity.ok(new Response("product fetched successfully", product));
    }

    @PostMapping("/products/bulk")
    public ResponseEntity<List<Product>> getProductsByIds(@RequestBody List<UUID> productIds) {
        List<Product> products = productService.getProductsByIds(productIds);
        return ResponseEntity.ok(products);
    }
}