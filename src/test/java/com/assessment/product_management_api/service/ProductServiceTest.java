package com.assessment.product_management_api.service;

import com.assessment.product_management_api.dto.ProductRequest;
import com.assessment.product_management_api.entity.Product;
import com.assessment.product_management_api.exception.NotFoundException;
import com.assessment.product_management_api.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll_returnsPagedProducts() {
        Product product = Product.builder()
                .id(1L)
                .productName("Test")
                .createdBy("user")
                .createdOn(LocalDateTime.now())
                .build();
        when(productRepository.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(product)));

        Page<?> page = productService.getAll(PageRequest.of(0, 10));

        assertEquals(1, page.getTotalElements());
    }

    @Test
    void getById_whenNotFound_throwsException() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.getById(1L));
    }

    @Test
    void create_persistsProduct() {
        ProductRequest request = new ProductRequest("Name");
        Product saved = Product.builder()
                .id(1L)
                .productName("Name")
                .createdBy("user")
                .createdOn(LocalDateTime.now())
                .build();

        when(productRepository.save(ArgumentMatchers.any(Product.class))).thenReturn(saved);

        var response = productService.create(request, "user");

        assertNotNull(response.id());
        assertEquals("Name", response.productName());
    }
}

