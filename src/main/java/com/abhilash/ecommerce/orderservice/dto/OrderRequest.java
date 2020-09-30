package com.abhilash.ecommerce.orderservice.dto;

import com.abhilash.ecommerce.orderservice.model.ShipmentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Abhilash Sulibela
 * <p>
 * This class encapsulates data transfer object to add a new order
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderRequest {
    private String customerId;
    private List<ItemDto> items;
    private ShipmentType shipmentType;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private int zip;
    //TODO: Consider abstracting Shipping Info. into a different entity
}
