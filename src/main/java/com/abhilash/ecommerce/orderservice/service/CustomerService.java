package com.abhilash.ecommerce.orderservice.service;

import com.abhilash.ecommerce.orderservice.dao.CustomerDao;
import com.abhilash.ecommerce.orderservice.exception.BadRequestException;
import com.abhilash.ecommerce.orderservice.model.Customer;
import com.abhilash.ecommerce.orderservice.repository.CustomerRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Service
public class CustomerService {
    private final CustomerRepo customerRepo;

    public void removeCustomer(String customerId) {
        if (!customerRepo.existsById(UUID.fromString(customerId))) {
            throw new BadRequestException("Invalid customerId - " + customerId);
        }
        customerRepo.deleteById(UUID.fromString(customerId));
    }

    public CustomerDao getCustomer(String customerId) {
        return mapToDao(customerRepo.findById(UUID.fromString(customerId))
                .orElseThrow(() -> new BadRequestException("Invalid customerId - " + customerId))
        );
    }

    public CustomerDao updateCustomer(String customerId, CustomerDao customerDao) {
        if (!customerRepo.existsById(UUID.fromString(customerId))) {
            throw new BadRequestException("Invalid customerId - " + customerId);
        }
        if (customerRepo.existsByEmail(customerDao.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        Customer customer = mapToEntity(customerDao);
        customer.setId(UUID.fromString(customerId));

        return mapToDao(customerRepo.save(customer));
    }

    public CustomerDao createCustomer(CustomerDao customerDao) {
        if (customerRepo.existsByEmail(customerDao.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        return mapToDao(customerRepo.save(mapToEntity(customerDao)));
    }

    private Customer mapToEntity(CustomerDao customerDao) {
        return Customer.builder()
                .name(customerDao.getName())
                .email(customerDao.getEmail())
                .build();
    }

    private CustomerDao mapToDao(Customer customer) {
        return CustomerDao.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .build();
    }
}
