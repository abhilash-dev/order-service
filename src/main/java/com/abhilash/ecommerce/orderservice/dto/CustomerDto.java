package com.abhilash.ecommerce.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author Abhilash Sulibela
 *
 * This class encapsulates data transfer object for Customer entity
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CustomerDto {
    private UUID id;
    private String name;
    private String email;
}
