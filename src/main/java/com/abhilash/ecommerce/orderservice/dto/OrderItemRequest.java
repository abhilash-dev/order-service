package com.abhilash.ecommerce.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * @author Abhilash Sulibela
 * <p>
 * This class encapsulates data transfer object to add / update / remove an item to an existing order
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderItemRequest {
    private UUID customerId;
    private List<ItemDto> items;
}
