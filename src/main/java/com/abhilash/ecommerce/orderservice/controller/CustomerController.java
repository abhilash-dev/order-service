package com.abhilash.ecommerce.orderservice.controller;

import com.abhilash.ecommerce.orderservice.dto.CustomerDto;
import com.abhilash.ecommerce.orderservice.service.CustomerService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Abhilash Sulibela
 * <p>
 * This class exposes resources to interact with Customer entity
 */
@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping(value = "/api/customer")
@Api(description = "Resources related to customer entity")
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerDto customerDto) {
        return ResponseEntity.ok(customerService.createCustomer(customerDto));
    }

    @GetMapping(value = "/{customerId}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable("customerId") String customerId) {
        return ResponseEntity.ok(customerService.getCustomer(customerId));
    }

    @PutMapping(value = "/{customerId}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable("customerId") String customerId, @RequestBody CustomerDto customerDto) {
        return ResponseEntity.ok(customerService.updateCustomer(customerId, customerDto));
    }

    @DeleteMapping(value = "/{customerId}")
    public ResponseEntity removeCustomer(@PathVariable("customerId") String customerId) {
        customerService.removeCustomer(customerId);
        return ResponseEntity.ok().build();
    }
}
