package com.example.bankapp.Filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.bankapp.services.CustomUserDetailService;
import com.example.bankapp.services.JWTservices;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	JWTservices jwtService;

	@Autowired
	ApplicationContext context;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String requestURI = request.getRequestURI();

		// Skip JWT + Redis validation for public endpoints
		if (requestURI.contains("/bankapp/main/login-account") || requestURI.contains("/bankapp/main/create")
				|| requestURI.contains("/bankapp/otp") || requestURI.contains("/swagger-ui") || // Swagger UI
				requestURI.contains("/v3/api-docs") || // OpenAPI JSON
				requestURI.contains("/swagger-resources") || // Swagger configs
				requestURI.contains("/webjars/")) {
			filterChain.doFilter(request, response);
			return;
		}
		// String redisToken = redisTemplate.opsForValue().get("session:" +
		// accountNumber);

		if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
			filterChain.doFilter(request, response);
			return;
		}

		System.out.println(">> JwtFilter triggered for path: " + request.getRequestURI());

		String authHeader = request.getHeader("Authorization");
		String token = null;
		String username = null;

		System.out.println(">> Authorization header(JWT filter): " + authHeader);

		token = jwtService.extractTokenFromRequest(request); // your existing logic
		
		if (token != null) {
			
			
			username = jwtService.extractUserName(token); // This should return the email (sub)
			System.out.println(">> Extracted username (JWT filter): " + username);

			String redisKey = "session:" + username;
			String storedToken = redisTemplate.opsForValue().get(redisKey);

			if (storedToken != null && storedToken.equals(token)) {
				// Valid session: setup AuthenticationContext
			} else {
				System.out.println(">> Token not found in Redis or session expired");
			}
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails userDetails = context.getBean(CustomUserDetailService.class).loadUserByUsername(username);

			if (jwtService.validateToken(token, userDetails)) {

				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());

				System.out.println("token verified");

				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);

			} else {
				System.out.println(">> Invalid token during validation");
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write("Invalid token");
				return;
			}
		}

		filterChain.doFilter(request, response);

	}

}
