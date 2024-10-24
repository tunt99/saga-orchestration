package com.saga.orchestration.command.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {
    private String productId;
    private String userId;
    private String addressId;
    private String paymentCardId;
    private String voucherId;
    private Integer quantity;
    private Double discountMoney;
    private Integer discountPercentage;
    private Double maxDiscountByPercentage;
}
