package com.example.bankapp.services;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IdempotencyService {

    private final RedisTemplate<String, Object> redisTemplate;
    
    
    public boolean isDuplicate(String key) {

        return Boolean.TRUE.equals(
                redisTemplate.hasKey(key)
        );
    }

    
    public void saveKey(String key) {

        redisTemplate.opsForValue()
                .set(key, "PROCESSED");
    }
}