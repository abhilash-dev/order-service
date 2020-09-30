package com.abhilash.ecommerce.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Payment {
    @Id
    @GeneratedValue
    private UUID id;
    private PaymentType type;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private int zip;
    //TODO: Consider adding more fields / customizing the same based on the PaymentType
}
