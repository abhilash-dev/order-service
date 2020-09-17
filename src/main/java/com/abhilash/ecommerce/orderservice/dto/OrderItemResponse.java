package com.abhilash.ecommerce.orderservice.dto;

import com.abhilash.ecommerce.orderservice.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * @author Abhilash Sulibela
 * <p>
 * This class encapsulates data transfer object for the user to reflect add / update / remove an item to an existing order
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderItemResponse {
    private UUID orderId;
    private OrderStatus orderStatus;
    private List<ItemDto> items;
}
