package com.saga.orchestration.constants.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    CREATED,
    COMPLETED,
    APPROVED,
    CANCELLED
}
