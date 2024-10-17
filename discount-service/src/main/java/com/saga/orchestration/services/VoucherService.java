package com.saga.orchestration.services;

import com.saga.orchestration.dtos.requests.CreateVoucherRequest;
import com.saga.orchestration.dtos.requests.ValidVoucherRequest;
import com.saga.orchestration.dtos.responses.ValidVoucherResponse;
import com.saga.orchestration.dtos.responses.VoucherDetailResponse;
import com.saga.orchestration.entities.Voucher;

public interface VoucherService {

    VoucherDetailResponse getVoucherDetail(String voucherId);

    Voucher createNewVoucher(CreateVoucherRequest request);

    ValidVoucherResponse checkValidVoucher(ValidVoucherRequest request);
}
