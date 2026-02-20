package com.assessment.product_management_api.service;

import com.assessment.product_management_api.dto.ProductRequest;
import com.assessment.product_management_api.dto.ProductResponse;
import com.assessment.product_management_api.entity.Product;
import com.assessment.product_management_api.exception.NotFoundException;
import com.assessment.product_management_api.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<ProductResponse> getAll(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(this::toResponse);
    }

    public ProductResponse getById(Long id) {
        Product product = findProductOrThrow(id);
        return toResponse(product);
    }

    public ProductResponse create(ProductRequest request, String username) {
        Product product = Product.builder()
                .productName(request.productName())
                .createdBy(username)
                .createdOn(LocalDateTime.now())
                .build();
        Product saved = productRepository.save(product);
        return toResponse(saved);
    }

    public ProductResponse update(Long id, ProductRequest request, String username) {
        Product product = findProductOrThrow(id);
        product.setProductName(request.productName());
        product.setModifiedBy(username);
        product.setModifiedOn(LocalDateTime.now());
        return toResponse(product);
    }

    public void delete(Long id) {
        Product product = findProductOrThrow(id);
        productRepository.delete(product);
    }

    protected Product findProductOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id " + id));
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getProductName(),
                product.getCreatedBy(),
                product.getCreatedOn(),
                product.getModifiedBy(),
                product.getModifiedOn()
        );
    }
}

