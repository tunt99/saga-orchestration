package com.saga.orchestration.services;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String hostUserMail;

    public void sendEmail(String to, String subject,  Map<String, Object> model) {
        try {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setFrom(hostUserMail);

        Context context = new Context();
        context.setVariables(model);
        String htmlContent = templateEngine.process("order-delivery", context);

        helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            log.info("Email sent successfully");
        } catch (Exception ex){
            log.info("Exception while sending email: {}", ex.getMessage());
        }
    }
}
