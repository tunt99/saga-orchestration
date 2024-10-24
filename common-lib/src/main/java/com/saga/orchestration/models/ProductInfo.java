package com.saga.orchestration.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductInfo {
    private String name;
    private Double price;
    private Integer quantity;
}
