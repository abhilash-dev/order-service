package com.abhilash.ecommerce.orderservice.repository;

import com.abhilash.ecommerce.orderservice.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, UUID> {
}
