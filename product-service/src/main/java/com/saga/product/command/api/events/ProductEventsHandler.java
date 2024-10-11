package com.saga.product.command.api.events;

import com.saga.product.command.api.data.Product;
import com.saga.product.command.api.data.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("product")
@RequiredArgsConstructor
public class ProductEventsHandler {

    private static final Logger log = LoggerFactory.getLogger(ProductEventsHandler.class);
    private final ProductRepository productRepository;

    @EventHandler
    public void on(ProductCreatedEvent event) throws Exception {

        try {
            Product product = new Product();
            BeanUtils.copyProperties(event, product);
            productRepository.save(product);
        } catch (Exception ex) {
            log.error("Exception Occurred {}", ex.getMessage());
            throw new Exception("Exception Occurred");
        }
    }

    @ExceptionHandler
    public void handle(Exception exception) throws Exception {
        throw exception;
    }
}
