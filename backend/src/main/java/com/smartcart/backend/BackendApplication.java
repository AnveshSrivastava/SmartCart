package com.smartcart.backend;

import com.smartcart.backend.model.User;
import com.smartcart.backend.repository.UserRepository;
import com.smartcart.backend.repository.ProductRepository;
import com.smartcart.backend.model.Product;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableMongoAuditing
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	// This will create an admin user if one does not exist
	@Bean
	public CommandLineRunner createAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			// Create admin user
			if (!userRepository.existsByEmail("admin@smartcart.com")) {
				User admin = new User();
				admin.setFullName("System Admin");
				admin.setEmail("admin@smartcart.com");
				admin.setPassword(passwordEncoder.encode("admin123"));
				admin.setRole(User.Role.ADMIN);
				userRepository.save(admin);
				System.out.println("✅ Admin user created: admin@smartcart.com / admin123");
			}
			
			// Create demo user
			if (!userRepository.existsByEmail("demo@smartcart.com")) {
				User demoUser = new User();
				demoUser.setFullName("Demo User");
				demoUser.setEmail("demo@smartcart.com");
				demoUser.setPassword(passwordEncoder.encode("demo123"));
				demoUser.setRole(User.Role.USER);
				userRepository.save(demoUser);
				System.out.println("✅ Demo user created: demo@smartcart.com / demo123");
			}
			
			// Create sample products if none exist
			if (productRepository.count() == 0) {
				createSampleProducts(productRepository);
			}
		};
	}
	
	@Bean
	public CommandLineRunner createSampleProducts(ProductRepository productRepository) {
		return args -> {
			// This will be called by the createAdmin method
		};
	}
	
	private void createSampleProducts(ProductRepository productRepository) {
		Product[] sampleProducts = {
			Product.builder()
				.title("iPhone 15 Pro")
				.description("Latest iPhone with advanced camera system and A17 Pro chip")
				.price(new java.math.BigDecimal("999.99"))
				.category("Electronics")
				.rating(4.8)
				.reviewCount(1250)
				.stock(50)
				.imageUrl("https://images.pexels.com/photos/788946/pexels-photo-788946.jpeg?auto=compress&cs=tinysrgb&w=400")
				.active(true)
				.build(),
			Product.builder()
				.title("Samsung Galaxy S24")
				.description("Premium Android smartphone with AI features")
				.price(new java.math.BigDecimal("899.99"))
				.category("Electronics")
				.rating(4.7)
				.reviewCount(890)
				.stock(75)
				.imageUrl("https://images.pexels.com/photos/1092644/pexels-photo-1092644.jpeg?auto=compress&cs=tinysrgb&w=400")
				.active(true)
				.build(),
			Product.builder()
				.title("MacBook Pro 16-inch")
				.description("Professional laptop with M3 Pro chip")
				.price(new java.math.BigDecimal("2499.99"))
				.category("Electronics")
				.rating(4.9)
				.reviewCount(567)
				.stock(25)
				.imageUrl("https://images.pexels.com/photos/18105/pexels-photo.jpg?auto=compress&cs=tinysrgb&w=400")
				.active(true)
				.build(),
			Product.builder()
				.title("Nike Air Max 270")
				.description("Comfortable running shoes with Air Max technology")
				.price(new java.math.BigDecimal("129.99"))
				.category("Sports")
				.rating(4.6)
				.reviewCount(2340)
				.stock(100)
				.imageUrl("https://images.pexels.com/photos/2529148/pexels-photo-2529148.jpeg?auto=compress&cs=tinysrgb&w=400")
				.active(true)
				.build(),
			Product.builder()
				.title("Adidas Ultraboost 22")
				.description("Premium running shoes with Boost midsole")
				.price(new java.math.BigDecimal("179.99"))
				.category("Sports")
				.rating(4.7)
				.reviewCount(1890)
				.stock(80)
				.imageUrl("https://images.pexels.com/photos/1464625/pexels-photo-1464625.jpeg?auto=compress&cs=tinysrgb&w=400")
				.active(true)
				.build(),
			Product.builder()
				.title("Sony WH-1000XM5")
				.description("Wireless noise-canceling headphones")
				.price(new java.math.BigDecimal("399.99"))
				.category("Electronics")
				.rating(4.8)
				.reviewCount(2100)
				.stock(60)
				.imageUrl("https://images.pexels.com/photos/3394650/pexels-photo-3394650.jpeg?auto=compress&cs=tinysrgb&w=400")
				.active(true)
				.build(),
			Product.builder()
				.title("Organic Cotton T-Shirt")
				.description("Comfortable organic cotton t-shirt in various colors")
				.price(new java.math.BigDecimal("29.99"))
				.category("Clothing")
				.rating(4.3)
				.reviewCount(456)
				.stock(200)
				.imageUrl("https://images.pexels.com/photos/1018911/pexels-photo-1018911.jpeg?auto=compress&cs=tinysrgb&w=400")
				.active(true)
				.build(),
			Product.builder()
				.title("Wireless Bluetooth Speaker")
				.description("Portable speaker with excellent sound quality")
				.price(new java.math.BigDecimal("79.99"))
				.category("Electronics")
				.rating(4.5)
				.reviewCount(789)
				.stock(150)
				.imageUrl("https://images.pexels.com/photos/1649771/pexels-photo-1649771.jpeg?auto=compress&cs=tinysrgb&w=400")
				.active(true)
				.build()
		};
		
		for (Product product : sampleProducts) {
			productRepository.save(product);
		}
		System.out.println("✅ Sample products created: " + sampleProducts.length + " products");
	}

}
