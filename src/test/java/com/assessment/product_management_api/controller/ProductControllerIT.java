package com.assessment.product_management_api.controller;

import com.assessment.product_management_api.dto.ProductRequest;
import com.assessment.product_management_api.entity.Role;
import com.assessment.product_management_api.entity.User;
import com.assessment.product_management_api.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@SpringBootTest
@Transactional
class ProductControllerIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private com.assessment.product_management_api.service.ProductService productService;

    @Test
    void createAndGetProduct_flow() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = User.builder()
                    .username("admin")
                    .email("admin@example.com")
                    .password("encoded-password")
                    .roles(Set.of(Role.ROLE_ADMIN))
                    .build();
            userRepository.save(admin);
        }

        ProductRequest request = new ProductRequest("Integration Product");

        var created = productService.create(request, "admin");
        var page = productService.getAll(org.springframework.data.domain.PageRequest.of(0, 10));

        org.junit.jupiter.api.Assertions.assertFalse(page.isEmpty());
        org.junit.jupiter.api.Assertions.assertTrue(
                page.stream().anyMatch(p -> p.id().equals(created.id()))
        );
    }
}

