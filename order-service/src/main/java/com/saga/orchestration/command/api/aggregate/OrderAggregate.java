package com.saga.orchestration.command.api.aggregate;

import com.saga.orchestration.commands.CancelOrderCommand;
import com.saga.orchestration.commands.CompleteOrderCommand;
import com.saga.orchestration.events.OrderCancelledEvent;
import com.saga.orchestration.events.OrderCompletedEvent;
import com.saga.orchestration.command.api.command.CreateOrderCommand;
import com.saga.orchestration.command.api.events.OrderCreatedEvent;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
@NoArgsConstructor
public class OrderAggregate {

    @AggregateIdentifier
    private String orderId;
    private String productId;
    private String userId;
    private String addressId;
    private String paymentCardId;
    private Double price;
    private Double totalPrice;
    private Integer quantity;
    private String orderStatus;

    /**
     *
     * Create Order
     */
    @CommandHandler
    public OrderAggregate(CreateOrderCommand createOrderCommand) {
        //Validate The Command
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
        BeanUtils.copyProperties(createOrderCommand, orderCreatedEvent);
        AggregateLifecycle.apply(orderCreatedEvent);
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent event) {
        this.orderStatus = event.getOrderStatus();
        this.userId = event.getUserId();
        this.orderId = event.getOrderId();
        this.quantity = event.getQuantity();
        this.productId = event.getProductId();
        this.addressId = event.getAddressId();
        this.paymentCardId = event.getPaymentCardId();
        this.price = event.getPrice();
        this.totalPrice = event.getTotalPrice();
    }

    /**
     * Completed Order
     */
    @CommandHandler
    public void handle(CompleteOrderCommand completeOrderCommand) {
        //Validate The Command
        // Publish Order Completed Event
        OrderCompletedEvent orderCompletedEvent = OrderCompletedEvent.builder()
                .orderStatus(completeOrderCommand.getOrderStatus())
                .orderId(completeOrderCommand.getOrderId())
                .userId(completeOrderCommand.getUserId())
                .build();
        AggregateLifecycle.apply(orderCompletedEvent);
    }

    @EventSourcingHandler
    public void on(OrderCompletedEvent event) {
        this.orderStatus = event.getOrderStatus();
    }

    /**
     * Cancel Order
     */
    @CommandHandler
    public void handle(CancelOrderCommand cancelOrderCommand) {
        OrderCancelledEvent orderCancelledEvent
                = new OrderCancelledEvent();
        BeanUtils.copyProperties(cancelOrderCommand,
                orderCancelledEvent);

        AggregateLifecycle.apply(orderCancelledEvent);
    }

    @EventSourcingHandler
    public void on(OrderCancelledEvent event) {
        this.orderStatus = event.getOrderStatus();
    }
}
