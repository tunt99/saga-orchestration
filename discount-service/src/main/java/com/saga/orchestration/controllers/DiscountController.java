package com.saga.orchestration.controllers;

import com.saga.orchestration.dtos.requests.CreateVoucherRequest;
import com.saga.orchestration.models.responses.ValidVoucherResponse;
import com.saga.orchestration.dtos.responses.VoucherDetailResponse;
import com.saga.orchestration.entities.Voucher;
import com.saga.orchestration.services.VoucherService;
import com.saga.orchestration.models.responses.base.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/discount/voucher")
@RequiredArgsConstructor
public class DiscountController {

    private final VoucherService voucherService;

    @GetMapping("/{voucherId}")
    public ApiResponse<VoucherDetailResponse> getVoucherDetail(@PathVariable("voucherId") String voucherId) {
        return ApiResponse.responseOK(voucherService.getVoucherDetail(voucherId));
    }

    @PostMapping
    public ApiResponse<Voucher> createNewVoucher(@RequestBody CreateVoucherRequest request) {
        return ApiResponse.responseOK(voucherService.createNewVoucher(request));
    }

    @GetMapping("/check/{voucherId}")
    public ApiResponse<ValidVoucherResponse> checkValidVoucher(@PathVariable("voucherId") String voucherId) {
        return ApiResponse.responseOK(voucherService.checkValidVoucher(voucherId));
    }
}
