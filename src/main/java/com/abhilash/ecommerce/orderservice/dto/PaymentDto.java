package com.abhilash.ecommerce.orderservice.dto;

import com.abhilash.ecommerce.orderservice.model.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author Abhilash Sulibela
 * <p>
 * This class encapsulates data transfer object to add / update / remove payment info. for a customer entity
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PaymentDto {
    private UUID id;
    private PaymentType type;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private int zip;
}
