package com.example.bankapp.aspect;

import java.util.DuplicateFormatFlagsException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import com.example.bankapp.annotation.Idempotent;
import com.example.bankapp.services.IdempotencyService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class IdempotencyAspect {

	private final IdempotencyService idempotencyService;

	private final HttpServletRequest httpServletRequest;

	@Around("@annotation(idempotent)")
	public Object handleIdempotency(ProceedingJoinPoint joinPoint, Idempotent idempotent) throws Throwable {
		String key = httpServletRequest.getHeader("Idempotency-Key");

		if (key == null || key.isBlank()) {

			throw new RuntimeException("Missing idempotncy key");
		}

		boolean locked = idempotencyService.lock(key, idempotent.ttl());

		if (!locked) {
			throw new DuplicateKeyException("Duplicate Request");
		}

		try {
			return joinPoint.proceed();
		} catch (Exception e) {
			idempotencyService.delete(key);

			throw e;
		}
	}

}
