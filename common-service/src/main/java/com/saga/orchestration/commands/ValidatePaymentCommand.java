package com.saga.orchestration.commands;

import com.saga.orchestration.models.UserInfo;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class ValidatePaymentCommand {

    @TargetAggregateIdentifier
    private String paymentId;
    private String orderId;
    private UserInfo.CardDetailInfo cardDetail;
    private String userId;
    private String paymentCardId;
    private Double price;
    private Double totalPrice;
}
