package com.assessment.product_management_api;

import com.assessment.product_management_api.entity.Role;
import com.assessment.product_management_api.entity.User;
import com.assessment.product_management_api.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class ProductManagementApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductManagementApiApplication.class, args);
	}

	@Bean
	CommandLineRunner initDefaultAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (userRepository.findByUsername("admin").isEmpty()) {
				User admin = User.builder()
						.username("admin")
						.email("admin@example.com")
						.password(passwordEncoder.encode("password"))
						.roles(Set.of(Role.ROLE_ADMIN, Role.ROLE_USER))
						.build();
				userRepository.save(admin);
			}
		};
	}
}
