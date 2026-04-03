package com.example.bankapp.Filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.bankapp.services.CustomAccountDetailService;
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
	
	private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String requestURI = request.getRequestURI();

		// for skipping public and swagger endpoint
		// for skiping OPTIONs of CROS
		if (isPublicEndpoint(requestURI) || isSwaggerEndpoint(requestURI) || "OPTIONS".equalsIgnoreCase(request.getMethod())) {

			filterChain.doFilter(request, response);
			return;
		}
		
		//log.debug("JwtFilter triggered for path: {}", requestURI);
		
		System.out.println(">> 	JwtFilter triggered for path: " + requestURI);

		String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {

			unauthorized(response, "  Missing or invalid Authorization header (JWT filter)");

			return;
		}

		String token = jwtService.extractTokenFromRequest(request);

		String userName = null;

		String email = jwtService.extractUserName(token);

		try {
			userName = jwtService.extractUserName(token);
			System.out.println(">>  Extracted UserName(JWT filter) :  " + userName);

		} catch (Exception e) {

			unauthorized(response, "Invalid Token");
			return;
			// TODO: handle exception
		}

		// 3 for Redis session now

		String redisKey = "session:" + email;

		String storedToken = redisTemplate.opsForValue().get(redisKey);

		if (storedToken == null || !storedToken.equals(token)) {

			System.out.println("Token not found in Redis or Token is Expired(JWT filter)");
			unauthorized(response, "Invalid Token or Redis Session Expired(JWT filter)");
			return;
		}

		// for Authentication Context

		if (SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails userDetails = context.getBean(CustomAccountDetailService.class).loadUserByUsername(userName);

			if (!jwtService.validateToken(token, userDetails)) {

				unauthorized(response, "Invalid Token (JWT filter)");
				return;
			}


			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
					null, userDetails.getAuthorities());

			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			//log.info("Token + Redis authenticated successfully");

			System.out.println("Token + Redis is authenticated Successfully(JWT filter)");
		}

		
	


//		// Skip JWT + Redis validation for public endpoints
//		if (requestURI.contains("/bankapp/main/login-account") || requestURI.contains("/bankapp/main/create")
//				|| requestURI.contains("/bankapp/account/changePinWithOtp")
//				|| requestURI.contains("/bankapp/account/changePasswordWithOtp") || requestURI.contains("/bankapp/otp")
//				|| requestURI.contains("/swagger-ui") || // Swagger UI
//				requestURI.contains("/v3/api-docs") || // OpenAPI JSON
//				requestURI.contains("/swagger-resources") || // Swagger configs
//				requestURI.contains("/webjars")) {
//			filterChain.doFilter(request, response);
//			return;
//		}
//		// String redisToken = redisTemplate.opsForValue().get("session:" +
//		// accountNumber);
//
//		if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
//			filterChain.doFilter(request, response);
//			return;
//		}
//
//		System.out.println(">>    JwtFilter triggered for path: " + request.getRequestURI());
//
//		String authHeader = request.getHeader("Authorization");
//		String token = null;
//		String username = null;
//
//		System.out.println(">>   Authorization header(JWT filter): " + authHeader);
//
//		token = jwtService.extractTokenFromRequest(request); // your existing logic
//
//		if (token != null) {
//
//			username = jwtService.extractUserName(token); // This should return the email (sub)
//			System.out.println(">>     Extracted username (JWT filter): " + username);
//
//			String redisKey = "session:" + username;
//			String storedToken = redisTemplate.opsForValue().get(redisKey);
//
//			if (storedToken != null && storedToken.equals(token)) {
//				// Valid session: setup AuthenticationContext
//			} else {
//				System.out.println(">>     Token not found in Redis or session expired");
//			}
//		}
//
//		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//			UserDetails userDetails = context.getBean(CustomAcountDetailService.class).loadUserByUsername(username);
//
//			if (jwtService.validateToken(token, userDetails)) {
//
//				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
//						userDetails, null, userDetails.getAuthorities());
//
//				System.out.println("token verified");
//
//				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//
//			} else {
//				System.out.println(">>     Invalid token during validation");
//				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//				response.getWriter().write("Invalid token");
//				return;
//			}
//		}
//
		filterChain.doFilter(request, response);

	}

	private boolean isPublicEndpoint(String url) {
		return url.startsWith("/bankapp/main/") || url.startsWith("/bankapp/otp/")
				|| url.startsWith("/bankapp/account/changePinWithOtp")
				|| url.startsWith("/bankapp/account/changePasswordWithOtp");

	}

	private boolean isSwaggerEndpoint(String url) {

		return url.startsWith("/bankapp/swagger-ui") || url.startsWith("/bankapp/v3/api-docs")
				|| url.startsWith("/bankapp/swagger-resources") || url.startsWith("/bankapp/webjars");
	}

	private void unauthorized(HttpServletResponse response, String message) throws IOException {
		response.reset();
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		response.getWriter().write("{\"error\": \"" + message + "\"}");
	}

}
