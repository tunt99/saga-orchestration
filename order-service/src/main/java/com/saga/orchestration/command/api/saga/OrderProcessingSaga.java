package com.saga.orchestration.command.api.saga;

import com.saga.orchestration.commands.*;
import com.saga.orchestration.events.*;
import com.saga.orchestration.models.UserInfo;
import com.saga.orchestration.queries.GetUserPaymentDetailsQuery;
import com.saga.orchestration.command.api.events.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@Saga
@Slf4j
public class OrderProcessingSaga {

    @Autowired
    private transient CommandGateway commandGateway;

    @Autowired
    private transient QueryGateway queryGateway;


    public OrderProcessingSaga() {
    }

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    private void handle(OrderCreatedEvent event) {
        log.info("OrderCreatedEvent in Saga for Order Id : {}", event.getOrderId());

        GetUserPaymentDetailsQuery getUserPaymentDetailsQuery
                = new GetUserPaymentDetailsQuery(event.getUserId());

        UserInfo user;
        List<UserInfo.CardDetailInfo> cardDetailInfo = null;

        try {
            user = queryGateway.query(getUserPaymentDetailsQuery, ResponseTypes.instanceOf(UserInfo.class)).join();
            cardDetailInfo = user.getCardDetails().stream().filter(c -> c.getCardId().equals(event.getPaymentCardId())).toList();
        } catch (Exception e) {
            log.error(e.getMessage());
            //Start the Compensating transaction
            cancelOrderCommand(event.getOrderId());
        }

        ValidatePaymentCommand validatePaymentCommand
                = ValidatePaymentCommand
                .builder()
                .paymentId(UUID.randomUUID().toString())
                .cardDetail(cardDetailInfo != null ? cardDetailInfo.get(0) : null)
                .orderId(event.getOrderId())
                .userId(event.getUserId())
                .paymentCardId(event.getPaymentCardId())
                .price(event.getPrice())
                .totalPrice(event.getTotalPrice())
                .build();

        commandGateway.sendAndWait(validatePaymentCommand);
    }

    private void cancelOrderCommand(String orderId) {
        CancelOrderCommand cancelOrderCommand
                = new CancelOrderCommand(orderId);
        commandGateway.send(cancelOrderCommand);
    }

    @SagaEventHandler(associationProperty = "orderId")
    private void handle(PaymentProcessedEvent event) {
        log.info("PaymentProcessedEvent in Saga for Order Id : {}", event.getOrderId());
        try {

            ShipOrderCommand shipOrderCommand = ShipOrderCommand.builder()
                    .shipmentId(UUID.randomUUID().toString())
                    .orderId(event.getOrderId())
                    .userId(event.getUserId())
                    .build();
            commandGateway.send(shipOrderCommand);
        } catch (Exception e) {
            log.error(e.getMessage());
            // Start the compensating transaction
            cancelPaymentCommand(event);
        }
    }

    private void cancelPaymentCommand(PaymentProcessedEvent event) {
        CancelPaymentCommand cancelPaymentCommand
                = new CancelPaymentCommand(
                event.getPaymentId(), event.getOrderId()
        );

        commandGateway.send(cancelPaymentCommand);
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderShippedEvent event) {

        log.info("OrderShippedEvent in Saga for Order Id : {}", event.getOrderId());

        CompleteOrderCommand completeOrderCommand = CompleteOrderCommand.builder()
                .orderId(event.getOrderId())
                .userId(event.getUserId())
                .orderStatus("APPROVED")
                .build();

        commandGateway.send(completeOrderCommand);
    }

    @SagaEventHandler(associationProperty = "orderId")
    @EndSaga
    public void handle(OrderCompletedEvent event) {
        log.info("OrderCompletedEvent in Saga for Order Id : {}", event.getOrderId());
    }

    @SagaEventHandler(associationProperty = "orderId")
    @EndSaga
    public void handle(OrderCancelledEvent event) {
        log.info("OrderCancelledEvent in Saga for Order Id : {}", event.getOrderId());
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(PaymentCancelledEvent event) {
        log.info("PaymentCancelledEvent in Saga for Order Id : {}", event.getOrderId());
        cancelOrderCommand(event.getOrderId());
    }
}
