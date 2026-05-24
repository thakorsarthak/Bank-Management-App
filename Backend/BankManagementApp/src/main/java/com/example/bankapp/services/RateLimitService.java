package com.example.bankapp.services;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RateLimitService {

	private final RedisTemplate<String, String> redisTemplate;


	public boolean isAllowed(String key , int maxRequests , Duration duration) {


		Long count = redisTemplate.opsForValue().increment(key);

		// First request → set expiry
		if (count != null && count == 1) {

			redisTemplate.expire(key, duration);
		}
	
	 return count != null &&
	            count <= maxRequests;
	}

}
