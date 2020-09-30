package com.abhilash.ecommerce.orderservice.dto;

import com.abhilash.ecommerce.orderservice.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author Abhilash Sulibela
 * <p>
 * This class encapsulates data transfer object to report status on an existing order
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderStatusResponse {
    private UUID orderId;
    private OrderStatus orderStatus;
}
