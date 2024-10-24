package com.saga.orchestration.consumers;

import com.saga.orchestration.configs.properties.DataProperties;
import com.saga.orchestration.models.OrderInfo;
import com.saga.orchestration.models.ProductInfo;
import com.saga.orchestration.models.UserInfo;
import com.saga.orchestration.service.RestServiceCommon;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saga.orchestration.services.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final DataProperties dataProperties;

    @KafkaListener(id = "${spring.kafka.consumer-group-id}", topics = "${spring.kafka.topic-send-mail-message}")
    public void receiveOrderAndSendMail(String message) {
        try {
            OrderInfo orderInfo = objectMapper.readValue(message, OrderInfo.class);

            ProductInfo productInfo = restServiceCommon.invokeApi(null, dataProperties.getFindProductById(),
                            HttpMethod.GET, null, null, ProductInfo.class, orderInfo.getProductId());

            UserInfo userInfo = restServiceCommon.invokeApi(null, dataProperties.getFindUserById(),
                    HttpMethod.GET, null, null, UserInfo.class, orderInfo.getUserId());
            log.info("=== KAFKA CONSUME ===: ORDER {} - PRODUCT {} - USER {}", orderInfo, productInfo, userInfo);

            Map<String, Object> model = new HashMap<>();
            model.put("customerName", userInfo.getFirstName() + " " + userInfo.getLastName());
            model.put("orderNumber", orderInfo.getOrderId());
            model.put("address", orderInfo.getAddressId());

            Map<String, Object> item = new HashMap<>();
            item.put("productName", productInfo.getName());
            item.put("quantity", orderInfo.getQuantity());
            double price = orderInfo.getQuantity() * productInfo.getPrice();
            item.put("price", "$" + price);

            model.put("orderItems", List.of(item));
            model.put("totalPrice", "$" + price);

            emailService.sendEmail("nguyentuantu2101@gmail.com", "Your Order has been Shipped!", model);

        } catch (Exception ex){
            log.info("Exception in ConsumeCompletedOrder: {}", ex.getMessage());
        }
    }
}
