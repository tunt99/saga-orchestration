package com.saga.orchestration.command.api.controller;

import com.saga.orchestration.command.api.command.CreateOrderCommand;
import com.saga.orchestration.command.api.model.OrderRestModel;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderCommandController {

    private final CommandGateway commandGateway;

    @PostMapping
    public String createOrder(@RequestBody OrderRestModel orderRestModel) {

        String orderId = UUID.randomUUID().toString();

        CreateOrderCommand createOrderCommand
                = CreateOrderCommand.builder()
                .orderId(orderId)
                .addressId(orderRestModel.getAddressId())
                .productId(orderRestModel.getProductId())
                .quantity(orderRestModel.getQuantity())
                .userId(orderRestModel.getUserId())
                .orderStatus("CREATED")
                .build();

        commandGateway.sendAndWait(createOrderCommand);

        return "Order Created";
    }
}
