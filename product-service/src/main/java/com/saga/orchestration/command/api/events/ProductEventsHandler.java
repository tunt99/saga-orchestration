package com.saga.orchestration.command.api.events;

import com.saga.orchestration.entities.Product;
import com.saga.orchestration.repositories.ProductRepository;
import com.saga.orchestration.constants.enums.ServicePrefix;
import com.saga.orchestration.service.RedisServiceCommon;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ProcessingGroup("product")
@RequiredArgsConstructor
public class ProductEventsHandler {

    private final ProductRepository productRepository;
    private final RedisServiceCommon redisServiceCommon;

    @EventHandler
    public void on(ProductCreatedEvent event) throws Exception {

        Product product = new Product();
        BeanUtils.copyProperties(event, product);
        productRepository.save(product);

        //Save product to redis
        redisServiceCommon.put(ServicePrefix.PRODUCT.name() + product.getProductId(), product.getQuantity());
    }

    @ExceptionHandler
    public void handle(Exception exception) throws Exception {
        throw exception;
    }
}
