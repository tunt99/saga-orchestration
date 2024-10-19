package com.saga.orchestration.services.impl;

import com.saga.orchestration.command.api.command.CreateOrderCommand;
import com.saga.orchestration.command.api.model.OrderRestModel;
import com.saga.orchestration.constants.enums.ServicePrefix;
import com.saga.orchestration.exception.BaseResponseException;
import com.saga.orchestration.service.RedisServiceCommon;
import com.saga.orchestration.services.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final RedisServiceCommon redisServiceCommon;
    private final CommandGateway commandGateway;

    private boolean isInInventory(String productId) {

        Integer quantity = (Integer) redisServiceCommon.get(ServicePrefix.PRODUCT.name() + productId);
        return quantity != null && quantity > 0;
    }

    @Override
    public String createOrder(OrderRestModel orderRestModel) {

        if (!isInInventory(orderRestModel.getProductId())) {
            throw new BaseResponseException("This product is out of inventory");
        }

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
