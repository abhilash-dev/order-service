package com.abhilash.ecommerce.orderservice.repository;

import com.abhilash.ecommerce.orderservice.model.CustomerAndPayment;
import com.abhilash.ecommerce.orderservice.util.CustomerPaymentId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerAndPaymentRepo extends JpaRepository<CustomerAndPayment, CustomerPaymentId> {
    List<CustomerAndPayment> findAllByCustomerId(String customerId);
}
