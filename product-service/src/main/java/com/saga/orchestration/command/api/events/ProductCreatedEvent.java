package com.saga.orchestration.command.api.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreatedEvent {

    private String productId;
    private String name;
    private Double price;
    private Integer quantity;
}
