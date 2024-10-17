package com.saga.orchestration.configs;

import com.saga.orchestration.interceptor.CustomClientHttpRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestTemplateConfig {

    @Bean(name = "appRestTemplate")
    public RestTemplate appRestTemplate() {

        SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        clientHttpRequestFactory.setReadTimeout(5000);

        RestTemplate  restClient = new RestTemplate(
                new BufferingClientHttpRequestFactory(clientHttpRequestFactory));

        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new CustomClientHttpRequestInterceptor());
        restClient.setInterceptors(interceptors);

        return restClient;
    }
}
