package com.abhilash.ecommerce.orderservice.util;

public class Constants {
    private Constants() {
        // empty private constructor to prevent instantiation
    }

    public static final String BOOTSTRAP_SERVERS_CONFIG = "127.0.0.1:9092";
    public static final String BULK_ORDER_TOPIC = "bulk_order";
    public static final String ORDER_REQUEST_GROUP_ID = "group_order_request";
    public static final String ORDER_REQUEST_CONTAINER_FACTORY = "orderRequestKafkaListenerFactory";
}
