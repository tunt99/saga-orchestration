package com.saga.orchestration.services.impl;

import com.saga.orchestration.configs.properties.DataProperties;
import com.saga.orchestration.dtos.requests.CreateVoucherRequest;
import com.saga.orchestration.dtos.requests.ValidVoucherRequest;
import com.saga.orchestration.dtos.responses.ValidVoucherResponse;
import com.saga.orchestration.dtos.responses.VoucherDetailResponse;
import com.saga.orchestration.entities.Voucher;
import com.saga.orchestration.models.UserRestModel;
import com.saga.orchestration.repositories.VoucherRepository;
import com.saga.orchestration.service.RestServiceCommon;
import com.saga.orchestration.services.VoucherService;
import com.saga.orchestration.exception.BaseResponseException;
import com.saga.orchestration.service.RedisServiceCommon;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
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
    private final RestServiceCommon restServiceCommon;
    private final DataProperties dataProperties;

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
    public ValidVoucherResponse checkValidVoucher(ValidVoucherRequest request) {
        UserRestModel userInfo = restServiceCommon.invokeApi(null, dataProperties.getFindUserById(),
                HttpMethod.GET, null, null, UserRestModel.class, request.getUserId());

        if (userInfo == null) {
            throw new BaseResponseException("User not found!");
        }

        VoucherDetailResponse voucherDetailResponse = this.getVoucherFromRedis(request.getVoucherId());

        if (voucherDetailResponse != null) {
            if (LocalDateTime.now().isBefore(voucherDetailResponse.getAvailableDateTime())) {
                throw new BaseResponseException("Voucher is not available yet!");
            }

            if (voucherDetailResponse.getAvailableRemainTime() < 0 ||
                    LocalDateTime.now().isAfter(voucherDetailResponse.getExpiredDateTime())) {
                throw new BaseResponseException("Voucher is expired!");
            }
        }

        return ValidVoucherResponse.builder()
                .voucherId(request.getVoucherId())
                .userId(request.getUserId())
                .isValid(true).build();
    }

    private VoucherDetailResponse getVoucherFromRedis(String voucherId) {
        try {
            Voucher voucher = voucherRepository.findById(voucherId)
                    .orElseThrow(() -> new BaseResponseException("Voucher not found!"));

            Long numberOfVouchers = (Long) redisServiceCommon.get(voucherId);
            if (numberOfVouchers == null) {
                throw new BaseResponseException("Voucher is used fully!");
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
