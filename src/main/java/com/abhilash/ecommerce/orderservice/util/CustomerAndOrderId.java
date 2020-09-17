package com.abhilash.ecommerce.orderservice.util;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class CustomerAndOrderId implements Serializable {
    private UUID customerId;
    private UUID orderId;
}
