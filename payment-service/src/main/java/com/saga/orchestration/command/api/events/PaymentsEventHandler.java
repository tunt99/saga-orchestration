package com.saga.orchestration.command.api.events;

import com.saga.orchestration.constants.enums.OrderStatus;
import com.saga.orchestration.events.PaymentCancelledEvent;
import com.saga.orchestration.events.PaymentProcessedEvent;
import com.saga.orchestration.command.api.data.Payment;
import com.saga.orchestration.command.api.data.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PaymentsEventHandler {

    private final PaymentRepository paymentRepository;

    @EventHandler
    public void on(PaymentProcessedEvent event) {
        Payment payment
                = Payment.builder()
                .paymentId(event.getPaymentId())
                .orderId(event.getOrderId())
                .paymentStatus(OrderStatus.COMPLETED.name())
                .timeStamp(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);
    }

    @EventHandler
    public void on(PaymentCancelledEvent event) {
        Payment payment = paymentRepository.findById(event.getPaymentId()).get();

        payment.setPaymentStatus(event.getPaymentStatus());

        paymentRepository.save(payment);
    }
}
