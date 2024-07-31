package com.dev.moim.global.redis.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisUtil {

    private final RedisTemplate<String, String> redisTemplate;

    public void setValue(String key, String value, Long expireInMillis) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, value, Duration.ofMillis(expireInMillis));
    }

    public String getValue(String key) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(key);
    }

    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }
}