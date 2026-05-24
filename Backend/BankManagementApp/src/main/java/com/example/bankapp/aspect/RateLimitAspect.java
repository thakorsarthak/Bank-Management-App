package com.example.bankapp.aspect;

import java.time.Duration;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.example.bankapp.Exception.CustomValidationException;
import com.example.bankapp.Exception.GlobalExceptionHandler;
import com.example.bankapp.annotation.RateLimited;
import com.example.bankapp.services.RateLimitService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitAspect {

	private final RateLimitService rateLimitService;

	private final HttpServletRequest request;

	@Around("@annotation(rateLimited)")
	public Object handleRateLimit(
			ProceedingJoinPoint joinPoint,
			RateLimited rateLimited) 
					throws Throwable {

		String ip = request.getRemoteAddr();

		if (ip.equals("0:0:0:0:0:0:0:1")) {
			ip = "127.0.0.1";
		}

		String redisKey = "rate-limit:" 
							+ rateLimited.prefix()
							+ ":" 
							+ ip;

		Duration duration = 
				Duration.ofMillis(
						rateLimited.timeUnit()
								.toMillis(
										rateLimited.duration()
										)
								);

		boolean allowed = rateLimitService.isAllowed(redisKey, rateLimited.limit(), duration);

		if (!allowed) {

			throw new CustomValidationException("Too many requests. Try again later.");
		}

		return joinPoint.proceed();

	}

}
