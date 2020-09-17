package com.abhilash.ecommerce.orderservice.model;

import com.abhilash.ecommerce.orderservice.util.CustomerPaymentId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(CustomerPaymentId.class)
public class CustomerAndPayment {
    @Id
    private UUID customerId;
    @Id
    private UUID paymentId;
}
