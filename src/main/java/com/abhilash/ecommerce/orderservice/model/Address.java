package com.abhilash.ecommerce.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Address {
    @Id
    @GeneratedValue
    private long id;

    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private CustomerOrder customer_order;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private int zip;
}
