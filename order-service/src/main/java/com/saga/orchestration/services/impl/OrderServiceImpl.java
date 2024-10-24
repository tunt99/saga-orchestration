package com.saga.orchestration.services.impl;

import com.saga.orchestration.command.api.command.CreateOrderCommand;
import com.saga.orchestration.command.api.model.CreateOrderRequest;
import com.saga.orchestration.configs.properties.DataProperties;
import com.saga.orchestration.constants.enums.ErrorCode;
import com.saga.orchestration.constants.enums.OrderStatus;
import com.saga.orchestration.constants.enums.ServicePrefix;
import com.saga.orchestration.exception.BusinessLogicException;
import com.saga.orchestration.models.ProductInfo;
import com.saga.orchestration.models.responses.ValidVoucherResponse;
import com.saga.orchestration.service.RedisServiceCommon;
import com.saga.orchestration.service.RestServiceCommon;
import com.saga.orchestration.services.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final RedisServiceCommon redisServiceCommon;
    private final CommandGateway commandGateway;
    private final RestServiceCommon restServiceCommon;
    private final DataProperties dataProperties;

    private boolean isInInventory(String productId) {

        Integer quantity = (Integer) redisServiceCommon.get(ServicePrefix.PRODUCT.name() + productId);
        return quantity != null && quantity > 0;
    }

    @Override
    public String createOrder(CreateOrderRequest request) {

        if (!isInInventory(request.getProductId())) {
            throw new BusinessLogicException(ErrorCode.PRODUCT_IS_OUT_OF_INVENTORY);
        }

        ValidVoucherResponse validResponse = restServiceCommon.invokeApi(null, dataProperties.getCheckValidVoucher(),
                HttpMethod.GET, null, null, ValidVoucherResponse.class, request.getVoucherId());

        if (!validResponse.getIsValid()){
            throw new BusinessLogicException(ErrorCode.VOUCHER_NOT_VALID);
        }

        ProductInfo productInfo = restServiceCommon.invokeApi(null, dataProperties.getFindProductById(),
                HttpMethod.GET, null, null, ProductInfo.class, request.getProductId());

        String orderId = UUID.randomUUID().toString();

        Double totalPrice = (productInfo.getPrice() * productInfo.getQuantity()) - request.getDiscountMoney();

        CreateOrderCommand createOrderCommand
                = CreateOrderCommand.builder()
                .orderId(orderId)
                .userId(request.getUserId())
                .addressId(request.getAddressId())
                .productId(request.getProductId())
                .paymentCardId(request.getPaymentCardId())
                .price(productInfo.getPrice())
                .totalPrice(totalPrice)
                .quantity(request.getQuantity())
                .orderStatus(OrderStatus.CREATED.name())
                .build();

        commandGateway.sendAndWait(createOrderCommand);

        return "Order Created";
    }
}
