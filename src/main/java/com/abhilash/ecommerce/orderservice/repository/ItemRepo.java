package com.abhilash.ecommerce.orderservice.repository;

import com.abhilash.ecommerce.orderservice.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ItemRepo extends JpaRepository<Item, UUID> {
}
