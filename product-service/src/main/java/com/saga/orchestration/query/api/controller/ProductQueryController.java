package com.saga.orchestration.query.api.controller;

import com.saga.orchestration.command.api.model.ProductRestModel;
import com.saga.orchestration.query.api.queries.GetAllProductsQuery;
import com.saga.orchestration.query.api.queries.GetProductQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductQueryController {

    private final QueryGateway queryGateway;

    @GetMapping
    public List<ProductRestModel> getAllProducts() {
        GetAllProductsQuery getAllProductsQuery = new GetAllProductsQuery();

        return queryGateway.query(getAllProductsQuery, ResponseTypes.multipleInstancesOf(ProductRestModel.class)).join();
    }

    @GetMapping("{productId}")
    public ProductRestModel getProductById(@PathVariable String productId) {
        GetProductQuery getProductQuery = new GetProductQuery(productId);

        return queryGateway.query(getProductQuery, ResponseTypes.instanceOf(ProductRestModel.class)).join();
    }
}
