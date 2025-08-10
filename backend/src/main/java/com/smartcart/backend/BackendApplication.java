package com.smartcart.backend;

import com.smartcart.backend.model.User;
import com.smartcart.backend.repository.UserRepository;
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
			if (!userRepository.existsByEmail("admin@smartcart.com")) {
				User admin = new User();
				admin.setFullName("System Admin");
				admin.setEmail("admin@smartcart.com");
				admin.setPassword(passwordEncoder.encode("admin123")); // Secure password
				admin.setRole(User.Role.ADMIN); // ✅ use enum instead of string
				userRepository.save(admin);
				System.out.println("✅ Admin user created: admin@smartcart.com / admin123");
			}
		};

}
}
