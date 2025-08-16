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
	public CommandLineRunner createAdmin(UserRepository userRepository, ProductRepository productRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			// Create admin user
			if (!userRepository.existsByEmail("admin@smartcart.com")) {
				User admin = User.builder()
					.fullName("System Admin")
					.email("admin@smartcart.com")
					.password(passwordEncoder.encode("admin123"))
					.role(User.Role.ADMIN)
					.build();
				userRepository.save(admin);
				System.out.println("✅ Admin user created: admin@smartcart.com / admin123");
			}

			// Create demo user
			if (!userRepository.existsByEmail("demo@smartcart.com")) {
				User demoUser = User.builder()
					.fullName("Demo User")
					.email("demo@smartcart.com")
					.password(passwordEncoder.encode("demo123"))
					.role(User.Role.USER)
					.build();
				userRepository.save(demoUser);
				System.out.println("✅ Demo user created: demo@smartcart.com / demo123");
			}
		};
	}

}
