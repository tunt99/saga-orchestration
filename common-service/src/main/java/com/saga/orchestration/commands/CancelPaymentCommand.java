package com.saga.orchestration.commands;

import com.saga.orchestration.constants.enums.OrderStatus;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
public class CancelPaymentCommand {

    @TargetAggregateIdentifier
    String paymentId;
    String orderId;
    String paymentStatus = OrderStatus.CANCELLED.name();
}
