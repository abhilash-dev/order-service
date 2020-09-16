package com.abhilash.ecommerce.orderservice.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OrderStatus {
    QUEUED, CREATED, PROCESSED, DENIED, CANCELLED
}
