package com.saga.orchestration.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidVoucherResponse {

    private String voucherId;
    private String userId;
    private Boolean isValid;
    private Double discountMoney;
    private Integer discountPercentage;
    private Double maxDiscountByPercentage;
}
