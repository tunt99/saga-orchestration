package com.saga.orchestration.consumers;

import com.saga.orchestration.service.RestServiceCommon;
import com.saga.orchestration.model.OrderRestModel;
import com.saga.orchestration.model.ProductRestModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saga.orchestration.model.UserRestModel;
import com.saga.orchestration.services.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConsumeCompletedOrder {

    private final ObjectMapper objectMapper;
    private final RestServiceCommon restServiceCommon;
    private final EmailService emailService;

    @Value("${find-product-by-id-url}")
    private String urlFindProductById;

    @Value("${find-user-by-id-url}")
    private String urlFindUserById;

    @KafkaListener(id = "${spring.kafka.consumer-group-id}", topics = "${spring.kafka.topic-send-mail-message}")
    public void receiveOrderAndSendMail(String message) {
        try {
            OrderRestModel orderInfo = objectMapper.readValue(message, OrderRestModel.class);

            ProductRestModel productInfo = restServiceCommon.invokeApi(null, urlFindProductById,
                            HttpMethod.GET, null, null, ProductRestModel.class, orderInfo.getProductId());

            UserRestModel userInfo = restServiceCommon.invokeApi(null, urlFindUserById,
                    HttpMethod.GET, null, null, UserRestModel.class, orderInfo.getUserId());
            log.info("=== KAFKA CONSUME ===: ORDER {} - PRODUCT {} - USER {}", orderInfo, productInfo, userInfo);

            Map<String, Object> model = new HashMap<>();
            model.put("customerName", userInfo.getFirstName() + " " + userInfo.getLastName());
            model.put("orderNumber", orderInfo.getOrderId());
            model.put("address", orderInfo.getAddressId());

            Map<String, Object> item = new HashMap<>();
            item.put("productName", productInfo.getName());
            item.put("quantity", orderInfo.getQuantity());
            double price = orderInfo.getQuantity() * productInfo.getPrice().doubleValue();
            item.put("price", "$" + price);

            model.put("orderItems", List.of(item));
            model.put("totalPrice", "$" + price);

            emailService.sendEmail("nguyentuantu2101@gmail.com", "Your Order has been Shipped!", model);

        } catch (Exception ex){
            log.info("Exception in ConsumeCompletedOrder: {}", ex.getMessage());
        }
    }
}
