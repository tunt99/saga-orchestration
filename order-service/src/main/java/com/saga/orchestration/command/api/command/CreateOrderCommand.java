package com.saga.orchestration.command.api.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class CreateOrderCommand {

    @TargetAggregateIdentifier
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
