package com.example.bankapp.services;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Duration;
import java.util.Base64;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
	
	private final RedisTemplate<String, String> redisTemplate;
	
	private String hashToken(String token) {
		
		try {
			
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			
			byte[] hash = digest.digest(
									token.getBytes(StandardCharsets.UTF_8));
			
			return Base64.getEncoder().encodeToString(hash);
			
		} catch (Exception e) {
			throw new RuntimeException("Error hasing token");
		}
		
	}
	
	
	public void storeRefreshToken(Long accountId , String refreshToken) {
		
		String redisKey = "refresh-token:" + accountId;
		
		String hashedToken = hashToken(refreshToken);
		
		redisTemplate.opsForValue().set(redisKey, hashedToken, Duration.ofDays(7));
	}

	
	public boolean validRefreshToken (Long accountId , String refreshToken) {
		
		String redisKey = "refresh-token:" + accountId;
		
		String storedHashed = redisTemplate.opsForValue().get(redisKey);
		
		if(storedHashed == null) {
			return false;
		}
		
		String incomingHash = hashToken(refreshToken);
		
		return storedHashed.equals(incomingHash);
	}
	
	public void deleteRefreshToken(Long accountId) {
		
		redisTemplate.delete("refresh-token:" + accountId);
		
	}
	
}
