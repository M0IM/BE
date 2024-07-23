package com.dev.moim.global.redis.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    public void setValue(String key, Object value, Long expireInMillis) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(key, value, Duration.ofMillis(expireInMillis));
    }

    public Object getValue(String key) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        return values.get(key);
    }

    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }
}