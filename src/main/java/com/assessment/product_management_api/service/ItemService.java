package com.assessment.product_management_api.service;

import com.assessment.product_management_api.dto.ItemRequest;
import com.assessment.product_management_api.dto.ItemResponse;
import com.assessment.product_management_api.entity.Item;
import com.assessment.product_management_api.entity.Product;
import com.assessment.product_management_api.exception.NotFoundException;
import com.assessment.product_management_api.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;
    private final ProductService productService;

    public ItemService(ItemRepository itemRepository, ProductService productService) {
        this.itemRepository = itemRepository;
        this.productService = productService;
    }

    public List<ItemResponse> getItemsByProductId(Long productId) {
        Product product = productService.findProductOrThrow(productId);
        return itemRepository.findByProduct(product)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ItemResponse getById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item not found with id " + id));
        return toResponse(item);
    }

    public ItemResponse addItemToProduct(Long productId, ItemRequest request) {
        Product product = productService.findProductOrThrow(productId);
        Item item = Item.builder()
                .product(product)
                .quantity(request.quantity())
                .build();
        Item saved = itemRepository.save(item);
        return toResponse(saved);
    }

    public ItemResponse updateItem(Long id, ItemRequest request) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item not found with id " + id));
        item.setQuantity(request.quantity());
        return toResponse(item);
    }

    public void deleteItem(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item not found with id " + id));
        itemRepository.delete(item);
    }

    private ItemResponse toResponse(Item item) {
        return new ItemResponse(
                item.getId(),
                item.getProduct().getId(),
                item.getQuantity()
        );
    }
}

