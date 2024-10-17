package com.saga.orchestration;

import com.saga.orchestration.configs.SagaCommonConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages = "com.saga")
@Import(SagaCommonConfig.class)
public class NotificationApp {

    public static void main(String[] args) {
        SpringApplication.run(NotificationApp.class, args);
    }

}
