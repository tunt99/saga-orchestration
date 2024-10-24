package com.saga.orchestration.services;

import com.saga.orchestration.command.api.model.CreateOrderRequest;

public interface OrderService {

    String createOrder(CreateOrderRequest createOrderRequest);
}
