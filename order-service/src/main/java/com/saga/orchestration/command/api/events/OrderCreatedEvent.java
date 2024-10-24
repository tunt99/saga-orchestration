package com.saga.orchestration.command.api.events;

import lombok.Data;

@Data
public class OrderCreatedEvent {

    private String orderId;
    private String productId;
    private String userId;
    private String addressId;
    private String paymentCardId;
    private Double price;
    private Double totalPrice;
    private Integer quantity;
    private String orderStatus;
}
