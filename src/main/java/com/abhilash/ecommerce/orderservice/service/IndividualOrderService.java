package com.abhilash.ecommerce.orderservice.service;

import com.abhilash.ecommerce.orderservice.dto.*;
import com.abhilash.ecommerce.orderservice.exception.BadRequestException;
import com.abhilash.ecommerce.orderservice.model.*;
import com.abhilash.ecommerce.orderservice.repository.*;
import com.abhilash.ecommerce.orderservice.util.CustomerAndOrderId;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class IndividualOrderService {
    private final OrderRepo orderRepo;
    private final OrderDetailRepo orderDetailRepo;
    private final ProductRepo productRepo;
    private final ItemRepo itemRepo;
    private final CustomerRepo customerRepo;
    private final CustomerAndOrderRepo customerAndOrderRepo;

    @Transactional
    public OrderStatusResponse createOrder(OrderRequest orderRequest) {
        if (!customerRepo.existsById(UUID.fromString(orderRequest.getCustomerId()))) {
            throw new BadRequestException("Invalid customer");
        }

        if (orderRequest.getItems().isEmpty()) {
            throw new BadRequestException("Bad Request: There must be at-least 1 item in the order");
        }

        OrderDetail orderDetail = new OrderDetail();
        List<Item> items = new ArrayList<>();
        double subTotal = 0.0;
        for (ItemDto itemDto : orderRequest.getItems()) {
            Product product = productRepo.findById(itemDto.getProductId()).orElseThrow(() -> new BadRequestException("One or more products are Invalid"));
            items.add(itemRepo.save(mapItemDtoToEntity(itemDto)));
            subTotal += (product.getPrice() * itemDto.getQuantity());
        }
        orderDetail.setSubTotal(subTotal);
        //TODO: expand the product with categories & assign tax brackets to them such that orderDetail can represent much practical value
        orderDetail.setTotal(subTotal);
        orderDetail.setItems(items);

        CustomerOrder order = buildOrderByShipmentType(orderRequest, orderDetail);
        CustomerOrder savedOrder = orderRepo.save(order);
        orderDetail.setId(savedOrder.getId());
        orderDetailRepo.save(orderDetail);

        customerAndOrderRepo.save(
                CustomerAndOrder.builder()
                        .customerId(UUID.fromString(orderRequest.getCustomerId()))
                        .orderId(savedOrder.getId())
                        .build()
        );

        return OrderStatusResponse.builder()
                .orderId(savedOrder.getId())
                .orderStatus(savedOrder.getStatus())
                .build();
    }

    @Transactional
    public OrderItemResponse addItemsToOrder(OrderItemRequest orderItemRequest) {
        if (!customerRepo.existsById(orderItemRequest.getCustomerId())) {
            throw new BadRequestException("Invalid customer");
        }

        CustomerOrder order = orderRepo.findById(orderItemRequest.getOrderId()).orElseThrow(() -> new BadRequestException("Invalid orderId"));

        CustomerAndOrderId customerAndOrderId = new CustomerAndOrderId();
        customerAndOrderId.setCustomerId(orderItemRequest.getCustomerId());
        customerAndOrderId.setOrderId(orderItemRequest.getOrderId());
        customerAndOrderRepo.findById(customerAndOrderId).orElseThrow(() -> new BadRequestException("Invalid orderId for customer"));

        OrderDetail orderDetail = orderDetailRepo.findById(order.getOrderDetailsId()).get();
        List<Item> newItems = orderItemRequest.getItems().parallelStream().map(this::mapItemDtoToEntity).map(this.itemRepo::save).collect(Collectors.toList());
        orderDetail.getItems().addAll(newItems);
        OrderDetail savedOrderDetails = orderDetailRepo.save(orderDetail);
        return OrderItemResponse.builder()
                .orderId(orderItemRequest.getOrderId())
                .items(savedOrderDetails.getItems().parallelStream().map(this::mapItemEntityToDTO).collect(Collectors.toList()))
                .orderStatus(order.getStatus())
                .build();

    }

    @Transactional
    public OrderItemResponse removeItemsFromOrder(OrderItemRequest orderItemRequest) {
        if (!customerRepo.existsById(orderItemRequest.getCustomerId())) {
            throw new BadRequestException("Invalid customer");
        }

        CustomerOrder order = orderRepo.findById(orderItemRequest.getOrderId()).orElseThrow(() -> new BadRequestException("Invalid orderId"));

        CustomerAndOrderId customerAndOrderId = new CustomerAndOrderId();
        customerAndOrderId.setCustomerId(orderItemRequest.getCustomerId());
        customerAndOrderId.setOrderId(orderItemRequest.getOrderId());
        customerAndOrderRepo.findById(customerAndOrderId).orElseThrow(() -> new BadRequestException("Invalid orderId for customer"));

        OrderDetail orderDetail = orderDetailRepo.findById(order.getOrderDetailsId()).get();

        orderItemRequest.getItems().parallelStream().map(this::mapItemDtoToEntity).forEach(
                inputItem -> {
                    Item itemToBeRemoved = itemRepo.findById(inputItem.getId()).orElseThrow(() -> new BadRequestException("One or more items are invalid"));
                    itemRepo.delete(itemToBeRemoved);
                }
        );
        orderDetail.setItems(orderDetail.getItems().parallelStream().filter(x -> itemRepo.existsById(x.getId())).collect(Collectors.toList()));
        OrderDetail savedOrderDetails = orderDetailRepo.save(orderDetail);

        if (savedOrderDetails.getItems().isEmpty()) {
            removeOrder(orderItemRequest.getOrderId(), orderItemRequest.getCustomerId());
            return OrderItemResponse.builder()
                    .orderId(orderItemRequest.getOrderId())
                    .items(Collections.emptyList())
                    .orderStatus(OrderStatus.PROCESSED)
                    .build();
        }

        return OrderItemResponse.builder()
                .orderId(orderItemRequest.getOrderId())
                .items(savedOrderDetails.getItems().parallelStream().map(this::mapItemEntityToDTO).collect(Collectors.toList()))
                .orderStatus(order.getStatus())
                .build();

    }

    public OrderStatusResponse removeOrder(UUID orderId, UUID customerId) {
        if (!customerRepo.existsById(customerId)) {
            throw new BadRequestException("Invalid customer");
        }

        CustomerOrder order = orderRepo.findById(orderId).orElseThrow(() -> new BadRequestException("Invalid orderId"));

        CustomerAndOrderId customerAndOrderId = new CustomerAndOrderId();
        customerAndOrderId.setCustomerId(customerId);
        customerAndOrderId.setOrderId(orderId);
        CustomerAndOrder customerAndOrder = customerAndOrderRepo.findById(customerAndOrderId).orElseThrow(() -> new BadRequestException("Invalid orderId for customer"));

        OrderDetail orderDetail = orderDetailRepo.findById(order.getOrderDetailsId()).get();
        orderDetail.getItems().forEach(item -> {
            itemRepo.deleteById(item.getId());
        });
        orderDetailRepo.delete(orderDetail);

        orderRepo.delete(order);

        customerAndOrderRepo.delete(customerAndOrder);

        return OrderStatusResponse.builder()
                .orderStatus(OrderStatus.PROCESSED)
                .orderId(orderId)
                .build();
    }

    public OrderDto getOrder(UUID orderId, UUID customerId) {
        if (!customerRepo.existsById(customerId)) {
            throw new BadRequestException("Invalid customer");
        }

        CustomerOrder order = orderRepo.findById(orderId).orElseThrow(() -> new BadRequestException("Invalid orderId"));

        CustomerAndOrderId customerAndOrderId = new CustomerAndOrderId();
        customerAndOrderId.setCustomerId(customerId);
        customerAndOrderId.setOrderId(orderId);
        customerAndOrderRepo.findById(customerAndOrderId).orElseThrow(() -> new BadRequestException("Invalid orderId for customer"));

        OrderDetail orderDetail = orderDetailRepo.findById(order.getOrderDetailsId()).get();

        return OrderDto.builder()
                .orderId(orderId)
                .orderStatus(order.getStatus())
                .shipmentType(order.getShipmentType())
                .items(orderDetail.getItems().parallelStream().map(this::mapItemEntityToDTO).collect(Collectors.toList()))
                .subTotal(orderDetail.getSubTotal())
                .tax(orderDetail.getTax())
                .total(orderDetail.getTotal())
                .addressLine1(order.getAddressLine1())
                .addressLine2(order.getAddressLine2())
                .city(order.getCity())
                .state(order.getState())
                .zip(order.getZip())
                .build();
    }

    public List<OrderDto> getAllOrders(UUID customerId) {
        //TODO: Implement later, just reuse the get single order after fetching all orders from mapping
        return Collections.emptyList();
    }

    private CustomerOrder buildOrderByShipmentType(OrderRequest orderRequest, OrderDetail savedOrderDetail) {
        //TODO: customize the creation of order based on Shipment Type
        CustomerOrder order;
        if (orderRequest.getShipmentType().equals(ShipmentType.HOME_DELIVERY)) {
            order = CustomerOrder.builder()
                    .customerId(UUID.fromString(orderRequest.getCustomerId()))
                    .status(OrderStatus.CREATED)
                    .shipmentType(orderRequest.getShipmentType())
                    .orderDetailsId(savedOrderDetail.getId())
                    .addressLine1(orderRequest.getAddressLine1())
                    .addressLine2(orderRequest.getAddressLine2())
                    .city(orderRequest.getCity())
                    .state(orderRequest.getState())
                    .zip(orderRequest.getZip())
                    .build();
        } else {
            order = CustomerOrder.builder()
                    .customerId(UUID.fromString(orderRequest.getCustomerId()))
                    .status(OrderStatus.CREATED)
                    .shipmentType(orderRequest.getShipmentType())
                    .orderDetailsId(savedOrderDetail.getId())
                    .build();
        }
        return order;
    }

    private Item mapItemDtoToEntity(ItemDto itemDto) {
        return Item.builder()
                .productId(itemDto.getProductId())
                .quantity(itemDto.getQuantity())
                .build();
    }

    private ItemDto mapItemEntityToDTO(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .productId(item.getProductId())
                .quantity(item.getQuantity())
                .build();
    }
}
