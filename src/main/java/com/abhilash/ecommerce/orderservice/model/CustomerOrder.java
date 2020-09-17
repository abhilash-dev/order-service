package com.abhilash.ecommerce.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Instant;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CustomerOrder {
    @Id
    @GeneratedValue
    private UUID id;
    private long customer_id;
    private OrderStatus status;
    private long orderDetailsId;
    private long paymentId;
    private long shippingAddressId;
    private ShipmentType shipmentType;
    private Instant createdDate;
    private Instant updatedDate;
}
