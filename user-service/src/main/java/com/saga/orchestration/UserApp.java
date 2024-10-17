package com.saga.orchestration;

import com.saga.orchestration.configs.SagaCommonConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(SagaCommonConfig.class)
public class UserApp {

	public static void main(String[] args) {
		SpringApplication.run(UserApp.class, args);
	}

}
