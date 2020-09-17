package com.abhilash.ecommerce.orderservice.controller;

import com.abhilash.ecommerce.orderservice.dto.*;
import com.abhilash.ecommerce.orderservice.service.IndividualOrderService;
import io.swagger.annotations.Api;
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
@Api(description = "Resources related to Order entity")
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

    @PostMapping(value = "/{orderId}/item")
    public ResponseEntity<OrderItemResponse> addItemsToOrder(@PathVariable("orderId") String orderId, @RequestBody OrderItemRequest orderItemRequest) {
        return ResponseEntity.ok(orderService.addItemsToOrder(orderId, orderItemRequest));
    }

    //TODO: Fix the foreign key constraint issue
    @DeleteMapping(value = "/{orderId}/item")
    public ResponseEntity<OrderItemResponse> deleteItemsFromOrder(@PathVariable("orderId") String orderId, @RequestBody OrderItemRequest orderItemRequest) {
        return ResponseEntity.ok(orderService.removeItemsFromOrder(orderId, orderItemRequest));
    }

    //TODO: Find a way to capture customerId for every request
    @DeleteMapping(value = "/{orderId}")
    public ResponseEntity<OrderStatusResponse> removeOrder(@PathVariable("orderId") String orderId, @RequestParam String customerId) {
        return ResponseEntity.ok(orderService.removeOrder(orderId, customerId));
    }

    //TODO: Find a way to capture customerId for every request
    @GetMapping(value = "/{orderId}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable("orderId") String orderId, @RequestParam String customerId) {
        return ResponseEntity.ok(orderService.getOrder(orderId, customerId));
    }

    //TODO: Implement Update order (Similar to other resources)
}
