package com.saga.orchestration.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoucherDetailResponse {

    private String voucherId;
    private String voucherName;
    private String voucherDescription;
    private LocalDateTime availableDateTime;
    private LocalDateTime expiredDateTime;
    private Double discountMoney;
    private Integer discountPercentage;
    private Double maxDiscountByPercentage;
    private Long availableRemainTime;
    private Long remainNumberOfVouchers;
}
