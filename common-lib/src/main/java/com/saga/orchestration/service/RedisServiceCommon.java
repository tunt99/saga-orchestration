package com.saga.orchestration.service;

import java.util.concurrent.TimeUnit;

public interface RedisServiceCommon {

    void put(String key, Object value, long time, TimeUnit timeUnit);

    void put(String key, Object value);

    Object get(String key);

}
