package com.saga.orchestration.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateVoucherRequest {

    private String voucherName;
    private String voucherDescription;
    private LocalDateTime availableDateTime;
    private LocalDateTime expiredDateTime;
    private Double discountMoney;
    private Integer discountPercentage;
}
