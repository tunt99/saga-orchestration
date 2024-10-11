package com.saga.product.query.api.projection;

import com.saga.product.command.api.data.Product;
import com.saga.product.command.api.data.ProductRepository;
import com.saga.product.command.api.model.ProductRestModel;
import com.saga.product.query.api.queries.GetAllProductsQuery;
import com.saga.product.query.api.queries.GetProductQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductProjection {

    private final ProductRepository productRepository;

    @QueryHandler
    public List<ProductRestModel> handle(GetAllProductsQuery getAllProductsQuery) {
        List<Product> products =
                productRepository.findAll();

        return products.stream()
                .map(product -> ProductRestModel
                        .builder()
                        .quantity(product.getQuantity())
                        .price(product.getPrice())
                        .name(product.getName())
                        .build())
                .collect(Collectors.toList());

    }

    @QueryHandler
    public ProductRestModel handle(GetProductQuery getProductQuery) {
        Product product = productRepository.findById(getProductQuery.getProductId()).orElseThrow();

        return ProductRestModel
                .builder()
                .quantity(product.getQuantity())
                .price(product.getPrice())
                .name(product.getName())
                .build();

    }
}
