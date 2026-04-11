package com.example.bankapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

import java.util.Base64;
import java.util.concurrent.TimeUnit;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.bankapp.services.JWTservices;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    private JWTservices jwtServices;

    @BeforeEach
    void setUp() throws Exception {
        // 1. Generate valid secret key
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
        SecretKey sk = keyGen.generateKey();
        String validSecretKey = Base64.getEncoder().encodeToString(sk.getEncoded());

        // 2. Mock Redis — lenient() stops Mockito complaining
        //    about stubs that aren't called in every test
        RedisTemplate<String, String> mockRedis = mock(RedisTemplate.class);
        ValueOperations<String, String> mockValueOps = mock(ValueOperations.class);

        lenient().when(mockRedis.opsForValue()).thenReturn(mockValueOps);
        lenient().doNothing().when(mockValueOps).set(
            anyString(),
            anyString(),
            anyLong(),
            any(TimeUnit.class)
        );

        // 3. Instantiate and inject both fields
        jwtServices = new JWTservices();

        var secretField = JWTservices.class.getDeclaredField("secretKey");
        secretField.setAccessible(true);
        secretField.set(jwtServices, validSecretKey);

        var redisField = JWTservices.class.getDeclaredField("redisTemplate");
        redisField.setAccessible(true);
        redisField.set(jwtServices, mockRedis);
    }

    @Test
    void shouldGenerateNonNullToken() {
        String token = jwtServices
            .generateToken("test@example.com", 1L, "ACC001", "USER");

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void shouldExtractCorrectUsernameFromToken() {
        String token = jwtServices
            .generateToken("test@example.com", 1L, "ACC001", "USER");

        assertEquals("test@example.com", jwtServices.extractUserName(token));
    }

    @Test
    void shouldExtractCorrectAccountNumberFromToken() {
        String token = jwtServices
            .generateToken("test@example.com", 1L, "ACC001", "USER");

        assertEquals("ACC001", jwtServices.extractAccountNumber(token));
    }

    @Test
    void shouldValidateTokenAgainstCorrectUser() {
        String token = jwtServices
            .generateToken("test@example.com", 1L, "ACC001", "USER");

        UserDetails userDetails = User.builder()
            .username("test@example.com")
            .password("password")
            .roles("USER")
            .build();

        assertTrue(jwtServices.validateToken(token, userDetails));
    }
}