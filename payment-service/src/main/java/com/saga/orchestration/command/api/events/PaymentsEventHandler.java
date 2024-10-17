package com.saga.orchestration.command.api.events;

import com.saga.orchestration.events.PaymentCancelledEvent;
import com.saga.orchestration.events.PaymentProcessedEvent;
import com.saga.orchestration.command.api.data.Payment;
import com.saga.orchestration.command.api.data.PaymentRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PaymentsEventHandler {

    private PaymentRepository paymentRepository;

    public PaymentsEventHandler(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @EventHandler
    public void on(PaymentProcessedEvent event) {
        Payment payment
                = Payment.builder()
                .paymentId(event.getPaymentId())
                .orderId(event.getOrderId())
                .paymentStatus("COMPLETED")
                .timeStamp(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);
    }

    @EventHandler
    public void on(PaymentCancelledEvent event) {
        Payment payment
                = paymentRepository.findById(event.getPaymentId()).get();

        payment.setPaymentStatus(event.getPaymentStatus());

        paymentRepository.save(payment);
    }
}
