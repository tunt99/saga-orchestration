package com.saga.orchestration;

import com.saga.orchestration.configs.SagaCommonConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = RedisRepositoriesAutoConfiguration.class)
@Import(SagaCommonConfig.class)
public class UserApp {

	public static void main(String[] args) {
		SpringApplication.run(UserApp.class, args);
	}

}
