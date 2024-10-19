package com.saga.orchestration.command.api.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductRestModel {
    private String name;
    private Double price;
    private Integer quantity;
}
