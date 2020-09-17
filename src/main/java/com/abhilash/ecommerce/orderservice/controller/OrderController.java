package com.abhilash.ecommerce.orderservice.controller;

import com.abhilash.ecommerce.orderservice.dto.*;
import com.abhilash.ecommerce.orderservice.service.IndividualOrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @author Abhilash Sulibela
 * <p>
 * This class exposes resources to interact with Order entity
 */
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/order")
public class OrderController {
    private final IndividualOrderService orderService;

    @PostMapping
    public ResponseEntity<OrderStatusResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        return ResponseEntity.ok(orderService.createOrder(orderRequest));
    }

    //TODO: Find a way to capture customerId for every request
    public ResponseEntity<List<OrderDto>> getAllOrders(@RequestParam String customerId) {
        return ResponseEntity.ok(orderService.getAllOrders(UUID.fromString(customerId)));
    }

    @PostMapping(value = "/item")
    public ResponseEntity<OrderItemResponse> addItemsToOrder(@RequestBody OrderItemRequest orderItemRequest) {
        return ResponseEntity.ok(orderService.addItemsToOrder(orderItemRequest));
    }

    @DeleteMapping(value = "/item")
    public ResponseEntity<OrderItemResponse> deleteItemsFromOrder(@RequestBody OrderItemRequest orderItemRequest) {
        return ResponseEntity.ok(orderService.removeItemsFromOrder(orderItemRequest));
    }

    //TODO: Find a way to capture customerId for every request
    @DeleteMapping(value = "/{orderId}")
    public ResponseEntity<OrderStatusResponse> removeOrder(@PathVariable("orderId") String orderId, @RequestParam String customerId) {
        return ResponseEntity.ok(orderService.removeOrder(UUID.fromString(orderId), UUID.fromString(customerId)));
    }

    //TODO: Find a way to capture customerId for every request
    @GetMapping(value = "/{orderId}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable("orderId") String orderId, @RequestParam String customerId) {
        return ResponseEntity.ok(orderService.getOrder(UUID.fromString(orderId), UUID.fromString(customerId)));
    }

    //TODO: Implement Update order (Similar to other resources)
}
