package com.example.bankapp.services;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IdempotencyService {

    private final RedisTemplate<String, String> redisTemplate;
    
    
    public boolean lock(String key) {
    	
    	String redisKey = "idempotency:" + key;
    	
    	Boolean success = 
    			redisTemplate.opsForValue().setIfAbsent(redisKey, "PROCESSING", Duration.ofMinutes(3));
    	
    	return Boolean.TRUE.equals(success);
    }
    
    public void delete(String key) {
    	
    	String redisKey = "idempotency" + key;
    	
    	redisTemplate.delete(redisKey);
    }
    
    
/*    public boolean isDuplicate(String key) {

        return Boolean.TRUE.equals(
                redisTemplate.hasKey(key)
        );
    }

    
    public void saveKey(String key) {

        redisTemplate.opsForValue()
                .set(key, "PROCESSED");
    }*/
}