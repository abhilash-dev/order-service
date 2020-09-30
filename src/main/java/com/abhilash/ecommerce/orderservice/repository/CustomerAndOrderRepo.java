package com.abhilash.ecommerce.orderservice.repository;

import com.abhilash.ecommerce.orderservice.model.CustomerAndOrder;
import com.abhilash.ecommerce.orderservice.util.CustomerAndOrderId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerAndOrderRepo extends JpaRepository<CustomerAndOrder, CustomerAndOrderId> {
    List<CustomerAndOrder> findAllByCustomerId(String customerId);
}
