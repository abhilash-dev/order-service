package com.abhilash.ecommerce.orderservice.repository;

import com.abhilash.ecommerce.orderservice.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderDetailRepo extends JpaRepository<OrderDetail, UUID> {
}
