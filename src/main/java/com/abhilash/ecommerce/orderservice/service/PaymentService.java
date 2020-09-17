package com.abhilash.ecommerce.orderservice.service;

import com.abhilash.ecommerce.orderservice.dto.PaymentDto;
import com.abhilash.ecommerce.orderservice.exception.BadRequestException;
import com.abhilash.ecommerce.orderservice.model.CustomerAndPayment;
import com.abhilash.ecommerce.orderservice.model.Payment;
import com.abhilash.ecommerce.orderservice.repository.CustomerAndPaymentRepo;
import com.abhilash.ecommerce.orderservice.repository.CustomerRepo;
import com.abhilash.ecommerce.orderservice.repository.PaymentRepo;
import com.abhilash.ecommerce.orderservice.util.CustomerPaymentId;
import lombok.AllArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class PaymentService {
    private final CustomerRepo customerRepo;
    private final PaymentRepo paymentRepo;
    private final CustomerAndPaymentRepo customerAndPaymentRepo;

    @Synchronized
    @Transactional
    public void removePayment(String paymentId, String customerId) {
        CustomerAndPayment customerAndPayment = customerAndPaymentRepo.findById(new CustomerPaymentId(UUID.fromString(customerId), UUID.fromString(paymentId))).orElseThrow(() -> new BadRequestException("Invalid paymentId - " + paymentId));
        paymentRepo.deleteById(customerAndPayment.getPaymentId());
        customerAndPaymentRepo.delete(customerAndPayment);
    }

    @Transactional
    public PaymentDto getPayment(String paymentId, String customerId) {
        CustomerAndPayment customerAndPayment = customerAndPaymentRepo.findById(new CustomerPaymentId(UUID.fromString(customerId), UUID.fromString(paymentId))).orElseThrow(() -> new BadRequestException("Invalid paymentId - " + paymentId));
        return mapToDto(paymentRepo.findById(customerAndPayment.getPaymentId()).get());
    }

    @Transactional
    public List<PaymentDto> getAllPayments(String customerId) {
        if (customerRepo.existsById(UUID.fromString(customerId))) {
            throw new BadRequestException("Invalid customer");
        }
        return customerAndPaymentRepo.findAllByCustomerId(customerId).parallelStream()
                .map(x -> paymentRepo.findById(x.getPaymentId()).get())
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Synchronized
    @Transactional
    public PaymentDto updatePayment(String customerId, String paymentId, PaymentDto paymentDto) {
        CustomerAndPayment customerAndPayment = customerAndPaymentRepo.findById(new CustomerPaymentId(UUID.fromString(customerId), UUID.fromString(paymentId))).orElseThrow(() -> new BadRequestException("Invalid paymentId - " + paymentId));

        Payment payment = mapToEntity(paymentDto);
        payment.setId(customerAndPayment.getPaymentId());

        return mapToDto(paymentRepo.save(payment));
    }

    @Synchronized
    @Transactional
    public PaymentDto createPayment(String customerId, PaymentDto paymentDto) {
        if (customerRepo.existsById(UUID.fromString(customerId))) {
            throw new BadRequestException("Invalid customer");
        }
        Payment savedPayment = paymentRepo.save(mapToEntity(paymentDto));
        CustomerAndPayment customerAndPayment = new CustomerAndPayment();
        customerAndPayment.setCustomerId(UUID.fromString(customerId));
        customerAndPayment.setPaymentId(savedPayment.getId());
        customerAndPaymentRepo.save(customerAndPayment);
        return mapToDto(savedPayment);
    }

    private Payment mapToEntity(PaymentDto paymentDto) {
        return Payment.builder()
                .type(paymentDto.getType())
                .addressLine1(paymentDto.getAddressLine1())
                .addressLine2(paymentDto.getAddressLine2())
                .city(paymentDto.getCity())
                .state(paymentDto.getState())
                .zip(paymentDto.getZip())
                .build();
    }

    private PaymentDto mapToDto(Payment payment) {
        return PaymentDto.builder()
                .id(payment.getId())
                .type(payment.getType())
                .addressLine1(payment.getAddressLine1())
                .addressLine2(payment.getAddressLine2())
                .city(payment.getCity())
                .state(payment.getState())
                .zip(payment.getZip())
                .build();
    }
}