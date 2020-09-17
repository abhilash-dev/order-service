package com.abhilash.ecommerce.orderservice.controller;

import com.abhilash.ecommerce.orderservice.dto.PaymentDto;
import com.abhilash.ecommerce.orderservice.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/customer/{customerId}")
public class PaymentController {
    private PaymentService paymentService;

    @PostMapping(value = "/payment")
    public ResponseEntity<PaymentDto> createPayment(@PathVariable("customerId") String customerId, @RequestBody PaymentDto paymentDto) {
        return ResponseEntity.ok(paymentService.createPayment(customerId, paymentDto));
    }

    @GetMapping(value = "/payment")
    public ResponseEntity<List<PaymentDto>> createPayment(@PathVariable("customerId") String customerId) {
        return ResponseEntity.ok(paymentService.getAllPayments(customerId));
    }

    @GetMapping(value = "/payment/{paymentId}")
    public ResponseEntity<PaymentDto> createPayment(@PathVariable("customerId") String customerId, @PathVariable("paymentId") String paymentId) {
        return ResponseEntity.ok(paymentService.getPayment(customerId, paymentId));
    }

    @PutMapping(value = "/payment/{paymentId}")
    public ResponseEntity<PaymentDto> updatePayment(@PathVariable("customerId") String customerId, @PathVariable("paymentId") String paymentId, @RequestBody PaymentDto paymentDto) {
        return ResponseEntity.ok(paymentService.updatePayment(customerId, paymentId, paymentDto));
    }

    @DeleteMapping(value = "/payment/{paymentId}")
    public ResponseEntity deletePayment(@PathVariable("customerId") String customerId, @PathVariable("paymentId") String paymentId) {
        paymentService.removePayment(paymentId, customerId);
        return ResponseEntity.ok().build();
    }
}
