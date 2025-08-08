package com.example.bankapp.services;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class JWTservices {
	
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	private String secretKey = "";

	public String extractTokenFromRequest(HttpServletRequest request) {
	    String authHeader = request.getHeader("Authorization");
	    System.out.println(authHeader+"authHeader");

	    if (authHeader != null && authHeader.startsWith("Bearer ")) {
	        return authHeader.substring(7); // Remove "Bearer " prefix
	    }

	    throw new RuntimeException("JWT token is missing or invalid");
	}

	public JWTservices() {

		try {
			KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");

			SecretKey sk = keyGen.generateKey();
		    System.out.println("JWT Service initialized with key: " + sk);

			secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
		    System.out.println("JWT Service initialized with key: " + secretKey);


		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public String generateToken(String email , String accountNumber) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("accountNumber", accountNumber);

		String token = Jwts.builder()
				.claims(claims)
				.subject(email)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
				.signWith(getKey())
				.compact();

		// return Jwts.builder() .claims() .add(claims) .subject(null);
		// return "token";
		
	 redisTemplate.opsForValue().set("session:" + accountNumber , token, 10 , TimeUnit.MINUTES );
	 
	 return token;
	}


	private SecretKey getKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public String extractUserName(String token) {
		return extractClaims(token, Claims::getSubject);
	}

	public String extractAccountNumber(String token) {
	    return extractClaims(token, claims -> claims.get("accountNumber").toString());
	}


	private <T> T extractClaims(String Token, Function<Claims, T> claimResolver) {

		final Claims claims = extractAllClaims(Token);
		return claimResolver.apply(claims);

	}

	private Claims extractAllClaims(String token) {

		return Jwts
				.parser()
				.verifyWith(getKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}

	public boolean validateToken(String token, UserDetails userDetails) {
		final String userName = extractUserName(token);
		System.out.println("From from jwtservice: "+userName);
		return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());

	}

	private Date extractExpiration(String token) {
		return extractClaims(token,Claims::getExpiration );

	}
}
