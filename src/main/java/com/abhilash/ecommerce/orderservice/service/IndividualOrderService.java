package com.abhilash.ecommerce.orderservice.service;

import com.abhilash.ecommerce.orderservice.dto.*;
import com.abhilash.ecommerce.orderservice.exception.BadRequestException;
import com.abhilash.ecommerce.orderservice.model.*;
import com.abhilash.ecommerce.orderservice.repository.*;
import com.abhilash.ecommerce.orderservice.util.CustomerAndOrderId;
import lombok.AllArgsConstructor;
import lombok.Synchronized;
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

    @Synchronized
    @Transactional
    public OrderStatusResponse createOrder(OrderRequest orderRequest) {
        log.trace("Entering createOrder method with {} ", orderRequest);
        log.debug("Received a new order request for customer - {}", orderRequest.getCustomerId());

        if (!customerRepo.existsById(UUID.fromString(orderRequest.getCustomerId()))) {
            log.error("BAD REQUEST: Invalid customer id - {}", orderRequest.getCustomerId());
            throw new BadRequestException("Invalid customer");
        }

        if (orderRequest.getItems().isEmpty()) {
            log.error("BAD REQUEST: There must be at-least 1 item in the order for customer - {}", orderRequest.getCustomerId());
            throw new BadRequestException("Bad Request: There must be at-least 1 item in the order");
        }

        OrderDetail orderDetail = new OrderDetail();
        List<Item> items = new ArrayList<>();
        double subTotal = 0.0;
        log.debug("Creating & saving {} - items for customer - {}", orderRequest.getItems(), orderRequest.getCustomerId());
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
        log.debug("New order is persisted with id - {} for customer - {}", savedOrder.getId(), orderRequest.getCustomerId());
        orderDetail.setId(savedOrder.getId());
        orderDetailRepo.save(orderDetail);
        log.debug("saved order details for order - {} ", savedOrder.getId());
        customerAndOrderRepo.save(
                CustomerAndOrder.builder()
                        .customerId(UUID.fromString(orderRequest.getCustomerId()))
                        .orderId(savedOrder.getId())
                        .build()
        );
        log.debug("saved an entry into CustomerAndOrder table with order - {} & customer - {}", savedOrder.getId(), orderRequest.getCustomerId());
        log.trace("Finishing createOrder method");
        return OrderStatusResponse.builder()
                .orderId(savedOrder.getId())
                .orderStatus(savedOrder.getStatus())
                .build();
    }

    @Synchronized
    @Transactional
    public OrderItemResponse addItemsToOrder(String orderId, OrderItemRequest orderItemRequest) {
        log.trace("Entering addItemsToOrder method with request - {}", orderItemRequest);
        log.debug("received a request to add {} items to order - {}", orderItemRequest.getItems().size(), orderId);
        if (!customerRepo.existsById(orderItemRequest.getCustomerId())) {
            log.error("BAD REQUEST: Invalid customer with customerId - ", orderItemRequest.getCustomerId());
            throw new BadRequestException("Invalid customer");
        }

        CustomerOrder order = orderRepo.findById(UUID.fromString(orderId)).orElseThrow(() -> new BadRequestException("Invalid orderId"));

        CustomerAndOrderId customerAndOrderId = new CustomerAndOrderId();
        customerAndOrderId.setCustomerId(orderItemRequest.getCustomerId());
        customerAndOrderId.setOrderId(UUID.fromString(orderId));
        customerAndOrderRepo.findById(customerAndOrderId).orElseThrow(() -> new BadRequestException("Invalid orderId for customer"));

        OrderDetail orderDetail = orderDetailRepo.findById(order.getId()).get();
        List<Item> newItems = orderItemRequest.getItems().parallelStream().map(this::mapItemDtoToEntity).map(this.itemRepo::save).collect(Collectors.toList());
        orderDetail.getItems().addAll(newItems);
        OrderDetail savedOrderDetails = orderDetailRepo.save(orderDetail);
        log.trace("Finishing addItemsToOrder method for order - {}", orderId);
        return OrderItemResponse.builder()
                .orderId(UUID.fromString(orderId))
                .items(savedOrderDetails.getItems().parallelStream().map(this::mapItemEntityToDTO).collect(Collectors.toList()))
                .orderStatus(order.getStatus())
                .build();

    }

    @Synchronized
    @Transactional
    public OrderItemResponse removeItemsFromOrder(String orderId, OrderItemRequest orderItemRequest) {
        log.trace("Entering removeItemsFromOrder with - {}", orderItemRequest);
        if (!customerRepo.existsById(orderItemRequest.getCustomerId())) {
            throw new BadRequestException("Invalid customer");
        }

        CustomerOrder order = orderRepo.findById(UUID.fromString(orderId)).orElseThrow(() -> new BadRequestException("Invalid orderId"));

        CustomerAndOrderId customerAndOrderId = new CustomerAndOrderId();
        customerAndOrderId.setCustomerId(orderItemRequest.getCustomerId());
        customerAndOrderId.setOrderId(UUID.fromString(orderId));
        customerAndOrderRepo.findById(customerAndOrderId).orElseThrow(() -> new BadRequestException("Invalid orderId for customer"));

        OrderDetail orderDetail = orderDetailRepo.findById(order.getId()).get();

        orderItemRequest.getItems().parallelStream().map(this::mapItemDtoToEntity).forEach(
                inputItem -> {
                    Item itemToBeRemoved = itemRepo.findById(inputItem.getId()).orElseThrow(() -> new BadRequestException("One or more items are invalid"));
                    itemRepo.delete(itemToBeRemoved);
                }
        );
        orderDetail.setItems(orderDetail.getItems().parallelStream().filter(x -> itemRepo.existsById(x.getId())).collect(Collectors.toList()));
        OrderDetail savedOrderDetails = orderDetailRepo.save(orderDetail);

        log.trace("Finishing removeItemsFromOrder for order - {}", orderId);
        if (savedOrderDetails.getItems().isEmpty()) {
            removeOrder(orderId, orderItemRequest.getCustomerId().toString());
            return OrderItemResponse.builder()
                    .orderId(UUID.fromString(orderId))
                    .items(Collections.emptyList())
                    .orderStatus(OrderStatus.PROCESSED)
                    .build();
        }

        return OrderItemResponse.builder()
                .orderId(UUID.fromString(orderId))
                .items(savedOrderDetails.getItems().parallelStream().map(this::mapItemEntityToDTO).collect(Collectors.toList()))
                .orderStatus(order.getStatus())
                .build();

    }

    @Synchronized
    @Transactional
    public OrderStatusResponse removeOrder(String orderId, String customerId) {
        if (!customerRepo.existsById(UUID.fromString(customerId))) {
            throw new BadRequestException("Invalid customer");
        }

        CustomerOrder order = orderRepo.findById(UUID.fromString(orderId)).orElseThrow(() -> new BadRequestException("Invalid orderId"));

        CustomerAndOrderId customerAndOrderId = new CustomerAndOrderId();
        customerAndOrderId.setCustomerId(UUID.fromString(customerId));
        customerAndOrderId.setOrderId(UUID.fromString(orderId));
        CustomerAndOrder customerAndOrder = customerAndOrderRepo.findById(customerAndOrderId).orElseThrow(() -> new BadRequestException("Invalid orderId for customer"));

        OrderDetail orderDetail = orderDetailRepo.findById(order.getId()).get();
        orderDetail.getItems().forEach(item -> {
            itemRepo.deleteById(item.getId());
        });
        orderDetailRepo.delete(orderDetail);

        orderRepo.delete(order);

        customerAndOrderRepo.delete(customerAndOrder);

        return OrderStatusResponse.builder()
                .orderStatus(OrderStatus.PROCESSED)
                .orderId(UUID.fromString(orderId))
                .build();
    }

    @Transactional
    public OrderDto getOrder(String orderId, String customerId) {
        if (!customerRepo.existsById(UUID.fromString(customerId))) {
            throw new BadRequestException("Invalid customer");
        }

        CustomerOrder order = orderRepo.findById(UUID.fromString(orderId)).orElseThrow(() -> new BadRequestException("Invalid orderId"));

        CustomerAndOrderId customerAndOrderId = new CustomerAndOrderId();
        customerAndOrderId.setCustomerId(UUID.fromString(customerId));
        customerAndOrderId.setOrderId(UUID.fromString(orderId));
        customerAndOrderRepo.findById(customerAndOrderId).orElseThrow(() -> new BadRequestException("Invalid orderId for customer"));

        OrderDetail orderDetail = orderDetailRepo.findById(order.getId()).get();

        return OrderDto.builder()
                .orderId(UUID.fromString(orderId))
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

    @Transactional
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
                    .build();
        }
        return order;
    }

    private Item mapItemDtoToEntity(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
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