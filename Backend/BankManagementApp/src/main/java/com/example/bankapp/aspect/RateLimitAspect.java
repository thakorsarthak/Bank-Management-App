package com.example.bankapp.aspect;

import java.time.Duration;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.example.bankapp.Exception.CustomValidationException;
import com.example.bankapp.Exception.GlobalExceptionHandler;
import com.example.bankapp.Exception.RateLimitExceededException;
import com.example.bankapp.annotation.RateLimited;
import com.example.bankapp.enums.RateLimitType;
import com.example.bankapp.services.JWTservice;
import com.example.bankapp.services.RateLimitService;
import com.twilio.jwt.Jwt;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitAspect {

	private final RateLimitService rateLimitService;
	
	private final JWTservice jwtService;

	private final HttpServletRequest request;

	@Around("@annotation(rateLimited)")
	public Object handleRateLimit(
			ProceedingJoinPoint joinPoint,
			RateLimited rateLimited) 
					throws Throwable {
		
		String identifier ;
		
		//Case 1 - IP BASED
		if(rateLimited.type() == RateLimitType.IP) {
			
			identifier = request.getRemoteAddr();

			if (identifier.equals("0:0:0:0:0:0:0:1")) {
				identifier = "127.0.0.1";
			}
			
		}
		
		//Case 2 -  ACCOUNT BASED
		
		else {
			String token = jwtService.extractTokenFromRequest(request);
			
			Long accountId = jwtService.extractAccountId(token);
			
			 identifier =
	                    String.valueOf(accountId);
		}
		
		String redisKey = "rate-limit:" 
							+ rateLimited.prefix()
							+ ":" 
							+ identifier;

		Duration duration = 
				Duration.ofMillis(
						rateLimited.timeUnit()
								.toMillis(
										rateLimited.duration()
										)
								);

		boolean allowed = rateLimitService.isAllowed(redisKey, rateLimited.limit(), duration);

		if (!allowed) {

			throw new RateLimitExceededException("Too many requests. Try again later.");
		}

		return joinPoint.proceed();
	}

}
