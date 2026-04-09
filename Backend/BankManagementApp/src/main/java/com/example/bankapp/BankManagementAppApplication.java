package com.example.bankapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableAsync;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
@EnableCaching
@EnableAsync
public class BankManagementAppApplication {
	
	 @Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	public static void main(String[] args) {
		SpringApplication.run(BankManagementAppApplication.class, args);
	}
	

    @PostConstruct
    public void testRedis() {
        try {
            redisTemplate.opsForValue().set("test", "working");
            System.out.println("✅ Redis connected successfully");
        } catch (Exception e) {
            System.out.println("❌ Redis connection failed: " + e.getMessage());
        }
    }
}
