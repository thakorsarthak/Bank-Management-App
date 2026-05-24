package com.example.bankapp.services;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountLockService {

	private final RedisTemplate<String, String> redisTemplate;
	
	private static final int MAX_ATTEMPTS = 5;
	
	private static final long LOCK_DURATION= 15;
	
	public boolean isLocked(Long accountId) {
		
		 String key = "account-lock:" + accountId;
		 
		 return Boolean.TRUE.equals(redisTemplate.hasKey(key));
	}
	
	public void loginFailed(Long accountId) {
		
		String failkey = "login-fail:" + accountId;
		
		Long attempts = redisTemplate.opsForValue().increment(failkey);
		
		//set expiry if first attempt
		if(attempts!=null && attempts == 1) {
			redisTemplate.expire(failkey, Duration.ofMinutes(15));
			
		}
		
		 if (attempts != null &&
		            attempts >= MAX_ATTEMPTS) {

		        lockAccount(accountId);
		    }
		
	}
	
	public void lockAccount(Long accountId) {
		
		String key = "account-lock:" + accountId;
		
	   redisTemplate.opsForValue().set(key,"LOCKED" , Duration.ofMinutes(LOCK_DURATION));
	}
	
	public void loginSucceeded(Long accountId) {

	    redisTemplate.delete("login-fail:" + accountId);
	}
	
}
