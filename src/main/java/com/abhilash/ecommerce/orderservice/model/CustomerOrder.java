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
    private UUID customerId;
    private OrderStatus status;
    //TODO: make this payment field OnetoMany in order to accomodate multiple payments for an individual customer for the same order
    private UUID paymentId;
    private ShipmentType shipmentType;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private int zip;
    private Instant createdAt;
    private Instant updatedAt;
}
