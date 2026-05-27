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

import com.example.bankapp.DTO.GlobalAPIResponseDTO;
import com.example.bankapp.services.CustomAccountDetailService;
import com.example.bankapp.services.JWTservice;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	JWTservice jwtService;

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
			return ;
		}

		//log.debug("JwtFilter triggered for path: {}", requestURI);

		System.out.println(">> 	JwtFilter triggered for path: " + requestURI);



		String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {

			unauthorized(response, "Missing or invalid Authorization header (JWT filter)");

			return;
		}

		String token = jwtService.extractTokenFromRequest(request);

		String userName = null;

		//String email = jwtService.extractUserName(token);
		
		Long accountId = jwtService.extractAccountId(token);

		try {
			userName = jwtService.extractUserName(token);
			System.out.println(">>  Extracted UserName(JWT filter) :  " + userName);

		} catch (Exception e) {

			unauthorized(response, "Invalid Token");
			return ;
			// TODO: handle exception
		}

		// 3 for Redis session now
		String redisKey = "session:" + accountId;
 
		String storedToken = redisTemplate.opsForValue().get(redisKey);

//		System.out.println("Extracted Email: " + email);
//		System.out.println("Redis Key Used: " + redisKey);
//		System.out.println("Token From Request: " + token);
//		System.out.println("Token From Redis: " + storedToken);

		
		/*No need to save redis key here we are storing while Login */
//		try {
//		    redisTemplate.opsForValue().set(redisKey, token);
//		    log.info("Redis save SUCCESS for key: {}", redisKey);
//		} catch (Exception e) {
//		    log.error("Redis save FAILED: {}", e.getMessage(), e);
//		    throw e;
//		}

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

		filterChain.doFilter(request, response);

	}

	private boolean isPublicEndpoint(String url) {
		return url.startsWith("/bankapp/main/") || url.startsWith("/bankapp/otp/")
				|| url.startsWith("/bankapp/account/changePinWithOtp")
				|| url.startsWith("/bankapp/account/changePasswordWithOtp")
				|| url.startsWith("/bankapp/account/refresh");

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
