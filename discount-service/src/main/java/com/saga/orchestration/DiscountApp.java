package com.saga.orchestration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.saga")
public class DiscountApp {

    public static void main(String[] args) {
        SpringApplication.run(DiscountApp.class, args);
    }

}
