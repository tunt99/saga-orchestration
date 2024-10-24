package com.saga.orchestration.service.impl;

import com.saga.orchestration.exception.BusinessLogicException;
import com.saga.orchestration.service.RestServiceCommon;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service(value = "RestServiceCommon")
@RequiredArgsConstructor
public class RestServiceCommonImpl implements RestServiceCommon {

    private final RestTemplate appRestTemplate;

    public <T> T invokeApi(String authorization, String url, HttpMethod method, Map<String, Object> params,
                           Object body, Class<T> targetResponse, Object... uriVariableValues) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, authorization);

        HttpEntity<Object> entity = new HttpEntity<>(body, httpHeaders);

        try {
            return appRestTemplate.exchange(url, method, entity, targetResponse, uriVariableValues).getBody();
        } catch (ResourceAccessException ex){
            log.info("Response timeout: {}", ex.getMessage());
            throw new ResourceAccessException(HttpStatus.REQUEST_TIMEOUT.getReasonPhrase());
        } catch (Exception ex){
            log.info("Response error: {}", ex.getMessage());
            throw new BusinessLogicException(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        }
    }
}
