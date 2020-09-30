package com.abhilash.ecommerce.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author Abhilash Sulibela
 * <p>
 * This class encapsulates data transfer object for Item entity
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ItemDto {
    private UUID id;
    private UUID productId;
    private long quantity;
}
