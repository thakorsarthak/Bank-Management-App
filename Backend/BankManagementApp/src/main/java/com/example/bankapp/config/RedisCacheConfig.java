package com.example.bankapp.config;

import java.time.Duration;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
public class RedisCacheConfig {
	
	public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
		
		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
				.entryTtl(Duration.ofMinutes(5))
				.disableCachingNullValues();
		
		return RedisCacheManager.builder(redisConnectionFactory)
				.cacheDefaults(config)
				.build();
	}

}
