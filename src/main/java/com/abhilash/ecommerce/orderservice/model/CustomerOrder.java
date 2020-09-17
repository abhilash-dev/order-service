package com.abhilash.ecommerce.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer_order")
public class CustomerOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;
    private OrderStatus status;
    private double total;
    private double tax;
    private double subTotal;
    @OneToOne(mappedBy = "customer_order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private OrderDetail orderDetail;
    @OneToOne(mappedBy = "customer_order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Payment payment;
    @OneToOne(mappedBy = "customer_order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Address billingAddress;
    @OneToOne(mappedBy = "customer_order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Address shippingAddress;
    private ShipmentType shipmentType;
    private Instant createdDate;
    private Instant updatedDate;
}
