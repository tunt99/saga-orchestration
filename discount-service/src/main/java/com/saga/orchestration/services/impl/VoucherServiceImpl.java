package com.saga.orchestration.services.impl;

import com.saga.orchestration.constants.enums.ErrorCode;
import com.saga.orchestration.dtos.requests.CreateVoucherRequest;
import com.saga.orchestration.models.responses.ValidVoucherResponse;
import com.saga.orchestration.dtos.responses.VoucherDetailResponse;
import com.saga.orchestration.entities.Voucher;
import com.saga.orchestration.repositories.VoucherRepository;
import com.saga.orchestration.services.VoucherService;
import com.saga.orchestration.exception.BusinessLogicException;
import com.saga.orchestration.service.RedisServiceCommon;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {

    private final VoucherRepository voucherRepository;
    private final RedisServiceCommon redisServiceCommon;

    @Override
    public VoucherDetailResponse getVoucherDetail(String voucherId) {
        return this.getVoucherFromRedis(voucherId);
    }

    @Override
    public Voucher createNewVoucher(CreateVoucherRequest request) {

        Voucher voucher = Voucher.builder()
                .voucherName(request.getVoucherName())
                .voucherDescription(request.getVoucherDescription())
                .availableDateTime(request.getAvailableDateTime())
                .expiredDateTime(request.getExpiredDateTime())
                .discountMoney(request.getDiscountMoney())
                .discountPercentage(request.getDiscountPercentage())
                .maxDiscountByPercentage(request.getMaxDiscountByPercentage())
                .numberOfVouchers(request.getNumberOfVouchers()).build();

        try {

            Duration durationWaiting = Duration.between(LocalDateTime.now(), request.getAvailableDateTime());
            Duration duration = Duration.between(request.getAvailableDateTime(), request.getExpiredDateTime());
            voucherRepository.save(voucher);

            redisServiceCommon.put(voucher.getVoucherId(), request.getNumberOfVouchers(),
                    durationWaiting.toSeconds() + duration.toSeconds(), TimeUnit.SECONDS);

            return voucherRepository.save(voucher);
        } catch (Exception ex) {
            log.info("Exception for voucher creation: {}", ex.getMessage());
            return null;
        }
    }

    @Override
    public ValidVoucherResponse checkValidVoucher(String voucherId) {

        VoucherDetailResponse voucherDetailResponse = this.getVoucherFromRedis(voucherId);

        if (voucherDetailResponse == null){
            throw new BusinessLogicException(ErrorCode.VOUCHER_NOT_FOUND);
        } else {
            if (LocalDateTime.now().isBefore(voucherDetailResponse.getAvailableDateTime())) {
                throw new BusinessLogicException(ErrorCode.VOUCHER_IS_NOT_AVAILABLE_YET);
            }

            if (voucherDetailResponse.getAvailableRemainTime() < 0 ||
                    LocalDateTime.now().isAfter(voucherDetailResponse.getExpiredDateTime())) {
                throw new BusinessLogicException(ErrorCode.VOUCHER_IS_EXPIRED);
            }
        }

        return ValidVoucherResponse.builder()
                .voucherId(voucherId)
                .isValid(true)
                .discountMoney(voucherDetailResponse.getDiscountMoney())
                .discountPercentage(voucherDetailResponse.getDiscountPercentage())
                .maxDiscountByPercentage(voucherDetailResponse.getMaxDiscountByPercentage()).build();
    }

    private VoucherDetailResponse getVoucherFromRedis(String voucherId) {
        try {
            Voucher voucher = voucherRepository.findById(voucherId)
                    .orElseThrow(() -> new BusinessLogicException(ErrorCode.VOUCHER_NOT_FOUND));

            Long numberOfVouchers = (Long) redisServiceCommon.get(voucherId);
            if (numberOfVouchers == null) {
                throw new BusinessLogicException(ErrorCode.VOUCHER_IS_USED_FULLY);
            }

            Duration durationWaiting = Duration.between(LocalDateTime.now(), voucher.getAvailableDateTime());
            Duration duration = Duration.between(voucher.getAvailableDateTime(), voucher.getExpiredDateTime());

            return VoucherDetailResponse.builder()
                    .voucherId(voucherId)
                    .voucherName(voucher.getVoucherName())
                    .voucherDescription(voucher.getVoucherDescription())
                    .availableDateTime(voucher.getAvailableDateTime())
                    .expiredDateTime(voucher.getExpiredDateTime())
                    .discountMoney(voucher.getDiscountMoney())
                    .discountPercentage(voucher.getDiscountPercentage())
                    .maxDiscountByPercentage(voucher.getMaxDiscountByPercentage())
                    .availableRemainTime(durationWaiting.toSeconds() + duration.toSeconds())
                    .remainNumberOfVouchers(numberOfVouchers)
                    .build();
        } catch (Exception ex) {
            log.error("Exception: {}", ex.getMessage());
            return null;
        }
    }
}
