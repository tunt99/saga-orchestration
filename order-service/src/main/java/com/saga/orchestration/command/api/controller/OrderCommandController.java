package com.saga.orchestration.command.api.controller;

import com.saga.orchestration.command.api.model.OrderRestModel;
import com.saga.orchestration.responses.ApiResponse;
import com.saga.orchestration.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderCommandController {

    private final OrderService orderService;

    @PostMapping
    public ApiResponse<String> createOrder(@RequestBody OrderRestModel orderRestModel) {
        return ApiResponse.responseOK(orderService.createOrder(orderRestModel));
    }
}
