package com.abhilash.ecommerce.orderservice.dto;

import com.abhilash.ecommerce.orderservice.model.OrderStatus;
import com.abhilash.ecommerce.orderservice.model.ShipmentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * @author Abhilash Sulibela
 * <p>
 * This class encapsulates data transfer object an Order to the end-user abstracting the internal schema
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderDto {
    private UUID orderId;
    private List<ItemDto> items;
    private OrderStatus orderStatus;
    private ShipmentType shipmentType;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private int zip;
    private double total;
    private double tax;
    private double subTotal;
}
