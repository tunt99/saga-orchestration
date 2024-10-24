package com.saga.orchestration.configs.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "system-url")
public class DataProperties {

    private String findUserById;
    private String findProductById;
    private String checkValidVoucher;
}
