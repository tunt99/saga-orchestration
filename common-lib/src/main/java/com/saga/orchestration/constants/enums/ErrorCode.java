package com.saga.orchestration.constants.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ErrorCode {
    VOUCHER_NOT_VALID("E00001", HttpStatus.BAD_REQUEST),
    PRODUCT_IS_OUT_OF_INVENTORY("E00002", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND("E00003", HttpStatus.BAD_REQUEST),
    VOUCHER_NOT_FOUND("E00004", HttpStatus.BAD_REQUEST),
    VOUCHER_IS_NOT_AVAILABLE_YET("E00005", HttpStatus.BAD_REQUEST),
    VOUCHER_IS_EXPIRED("E00006", HttpStatus.BAD_REQUEST),
    VOUCHER_IS_USED_FULLY("E00007", HttpStatus.BAD_REQUEST),
    ;

    private String messageCode;
    private HttpStatusCode statusCode;
}
