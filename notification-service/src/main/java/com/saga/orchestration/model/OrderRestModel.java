package com.saga.orchestration.model;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRestModel {
    private String orderId;
    private String productId;
    private String userId;
    private String addressId;
    private Integer quantity;
    private String orderStatus;
}
