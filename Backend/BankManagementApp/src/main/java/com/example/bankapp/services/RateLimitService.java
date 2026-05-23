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

	private static final int MAX_REQUESTS = 5;

	private static final long WINDOW_MINUTES = 1;

	public boolean isAllowed(String ip) {

		String key = "rate-limit:login:" + ip;

		Long count = redisTemplate.opsForValue().increment(key);

		// First request → set expiry
		if (count != null && count == 1) {

			redisTemplate.expire(key, Duration.ofMinutes(WINDOW_MINUTES));
		}
	
	 return count != null &&
	            count <= MAX_REQUESTS;
	}

}
