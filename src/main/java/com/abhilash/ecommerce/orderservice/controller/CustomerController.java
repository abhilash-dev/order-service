package com.abhilash.ecommerce.orderservice.controller;

import com.abhilash.ecommerce.orderservice.dao.CustomerDao;
import com.abhilash.ecommerce.orderservice.service.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping(value = "/api/customer")
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerDao> createCustomer(@RequestBody CustomerDao customerDao) {
        return ResponseEntity.ok(customerService.createCustomer(customerDao));
    }

    @GetMapping(value = "/{customerId}")
    public ResponseEntity<CustomerDao> getCustomer(@PathVariable("customerId") String customerId) {
        return ResponseEntity.ok(customerService.getCustomer(customerId));
    }

    @PutMapping(value = "/{customerId}")
    public ResponseEntity<CustomerDao> updateCustomer(@PathVariable("customerId") String customerId, @RequestBody CustomerDao customerDao) {
        return ResponseEntity.ok(customerService.updateCustomer(customerId, customerDao));
    }

    @DeleteMapping(value = "/{customerId}")
    public ResponseEntity removeCustomer(@PathVariable("customerId") String customerId) {
        customerService.removeCustomer(customerId);
        return ResponseEntity.ok().build();
    }
}
