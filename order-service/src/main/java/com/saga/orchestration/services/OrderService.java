package com.saga.orchestration.services;

import com.saga.orchestration.command.api.model.OrderRestModel;

public interface OrderService {

    String createOrder(OrderRestModel orderRestModel);
}
