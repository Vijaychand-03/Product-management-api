package com.assessment.product_management_api.repository;

import com.assessment.product_management_api.entity.Item;
import com.assessment.product_management_api.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByProduct(Product product);
}

