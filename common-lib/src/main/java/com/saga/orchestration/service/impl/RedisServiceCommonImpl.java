package com.saga.orchestration.service.impl;

import com.saga.orchestration.service.RedisServiceCommon;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service(value = "RedisServiceCommon")
@RequiredArgsConstructor
public class RedisServiceCommonImpl implements RedisServiceCommon {

    private final RedisTemplate<String, Object> sagaRedisTemplate;

    @Override
    public void put(String key, Object value, long time, TimeUnit timeUnit) {
        sagaRedisTemplate.opsForValue().set(key, value, time, timeUnit);

    }

    @Override
    public void put(String key, Object value) {
        sagaRedisTemplate.opsForValue().set(key, value);
    }

    @Override
    public Object get(String key) {
        return sagaRedisTemplate.opsForValue().get(key);
    }
}
