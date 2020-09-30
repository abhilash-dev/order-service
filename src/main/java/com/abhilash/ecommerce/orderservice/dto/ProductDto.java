package com.abhilash.ecommerce.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author Abhilash Sulibela
 * <p>
 * This class encapsulates data transfer object to add / update / remove a product from the inventory
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductDto {
    private UUID id;
    private double price;
    private String name;
}
