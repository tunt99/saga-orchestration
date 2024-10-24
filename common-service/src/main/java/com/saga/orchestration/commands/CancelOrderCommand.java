package com.saga.orchestration.commands;

import com.saga.orchestration.constants.enums.OrderStatus;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
public class CancelOrderCommand {

    @TargetAggregateIdentifier
    String orderId;
    String orderStatus = OrderStatus.CANCELLED.name();
}
