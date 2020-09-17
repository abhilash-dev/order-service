package com.abhilash.ecommerce.orderservice.service;

import com.abhilash.ecommerce.orderservice.dto.BulkOrderRequest;
import com.abhilash.ecommerce.orderservice.dto.OrderRequest;
import com.abhilash.ecommerce.orderservice.dto.OrderStatusResponse;
import com.abhilash.ecommerce.orderservice.repository.OrderRepo;
import com.abhilash.ecommerce.orderservice.util.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class BulkOrderService {
    // TODO: Refactor the kafka related methods to it's own service
    private final KafkaTemplate<String, OrderRequest> kafkaTemplate;
    private final OrderRepo orderRepo;

    public List<OrderStatusResponse> createBulkOrder(List<OrderRequest> orderRequestList) {
        //TODO: Create an empty order with status queued
        //TODO: Make an entry in CustomerAndOrder for the same
        //TODO: Create a BulkOrderRequest & push it in to the queue
        //TODO: Return the OrderStatusResponse list to the user
        return Collections.emptyList();
    }

    @KafkaListener(topics = Constants.BULK_ORDER_TOPIC, groupId = Constants.ORDER_REQUEST_GROUP_ID, containerFactory = Constants.ORDER_REQUEST_CONTAINER_FACTORY)
    private void processBulkOrder(BulkOrderRequest bulkOrderRequest) {
        //TODO:Follow a smilar pipeline to that of Individual order processing with only differnce being the following
        //TODO: 1. We have already bootstrapped the order with a Queued status
        //TODO: 2. We have already made an entry in CustomerAndOrder table for the association
    }
}
