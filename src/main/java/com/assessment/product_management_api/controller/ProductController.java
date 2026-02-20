package com.assessment.product_management_api.controller;

import com.assessment.product_management_api.dto.ItemRequest;
import com.assessment.product_management_api.dto.ItemResponse;
import com.assessment.product_management_api.dto.ProductRequest;
import com.assessment.product_management_api.dto.ProductResponse;
import com.assessment.product_management_api.service.ItemService;
import com.assessment.product_management_api.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Products", description = "Product management APIs")
public class ProductController {

    private final ProductService productService;
    private final ItemService itemService;

    public ProductController(ProductService productService, ItemService itemService) {
        this.productService = productService;
        this.itemService = itemService;
    }

    @GetMapping
    @Operation(summary = "Get all products with pagination")
    public ResponseEntity<Page<ProductResponse>> getProducts(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(productService.getAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by id")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new product")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request,
                                                         Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "system";
        ProductResponse response = productService.create(request, username);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing product")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,
                                                         @Valid @RequestBody ProductRequest request,
                                                         Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "system";
        ProductResponse response = productService.update(id, request, username);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/items")
    @Operation(summary = "Get items for a product")
    public ResponseEntity<List<ItemResponse>> getItemsForProduct(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.getItemsByProductId(id));
    }

    @PostMapping("/{id}/items")
    @Operation(summary = "Add an item to a product")
    public ResponseEntity<ItemResponse> addItemToProduct(@PathVariable Long id,
                                                         @Valid @RequestBody ItemRequest request) {
        ItemResponse response = itemService.addItemToProduct(id, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}

