package com.abhilash.ecommerce.orderservice.service;

import com.abhilash.ecommerce.orderservice.dto.CustomerDto;
import com.abhilash.ecommerce.orderservice.exception.BadRequestException;
import com.abhilash.ecommerce.orderservice.model.Customer;
import com.abhilash.ecommerce.orderservice.repository.CustomerRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Service
public class CustomerService {
    private final CustomerRepo customerRepo;

    @Transactional
    public void removeCustomer(String customerId) {
        if (!customerRepo.existsById(UUID.fromString(customerId))) {
            throw new BadRequestException("Invalid customerId - " + customerId);
        }
        customerRepo.deleteById(UUID.fromString(customerId));
    }

    public CustomerDto getCustomer(String customerId) {
        return mapToDao(customerRepo.findById(UUID.fromString(customerId))
                .orElseThrow(() -> new BadRequestException("Invalid customerId - " + customerId))
        );
    }

    @Transactional
    public CustomerDto updateCustomer(String customerId, CustomerDto customerDto) {
        if (!customerRepo.existsById(UUID.fromString(customerId))) {
            throw new BadRequestException("Invalid customerId - " + customerId);
        }
        if (customerRepo.existsByEmail(customerDto.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        Customer customer = mapToEntity(customerDto);
        customer.setId(UUID.fromString(customerId));

        return mapToDao(customerRepo.save(customer));
    }

    @Transactional
    public CustomerDto createCustomer(CustomerDto customerDto) {
        if (customerRepo.existsByEmail(customerDto.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        return mapToDao(customerRepo.save(mapToEntity(customerDto)));
    }

    private Customer mapToEntity(CustomerDto customerDto) {
        return Customer.builder()
                .name(customerDto.getName())
                .email(customerDto.getEmail())
                .build();
    }

    private CustomerDto mapToDao(Customer customer) {
        return CustomerDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .build();
    }
}
