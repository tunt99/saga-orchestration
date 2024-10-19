package com.saga.orchestration.commands;

import com.saga.orchestration.model.UserInfo;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.List;

@Data
@Builder
public class ValidatePaymentCommand {

    @TargetAggregateIdentifier
    private String paymentId;
    private String orderId;
    private List<UserInfo.CardDetailInfo> cardDetails;
    private String userId;
}
