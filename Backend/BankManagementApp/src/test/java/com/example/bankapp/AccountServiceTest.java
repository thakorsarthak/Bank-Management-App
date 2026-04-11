package com.example.bankapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.bankapp.DTO.AccountLoginDTO;
import com.example.bankapp.DTO.AccountSignUpDTO;
import com.example.bankapp.entity.Account;
import com.example.bankapp.implementation.AccountServiceImp;
import com.example.bankapp.repository.AccountRepo;
import com.example.bankapp.repository.BranchRepository;
import com.example.bankapp.repository.TransactionRepo;
import com.example.bankapp.services.JWTservices;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepo accountRepo;

    @Mock
    private BranchRepository branchRepo;

    @Mock
    private JWTservices jwtServices;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TransactionRepo transactionRepo;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @InjectMocks
    private AccountServiceImp accountService;

    // ─── Test 1 ───────────────────────────────────────────
    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        AccountSignUpDTO dto = new AccountSignUpDTO();
        dto.setEmail("test@example.com");
        dto.setContact("7899758769L");
        dto.setAadharNo("123456789012");
        dto.setPanNo("ABCDE1234F");
        dto.setPin("1234");
        dto.setConfirmPin("1234");

        when(accountRepo.existsByEmail("test@example.com")).thenReturn(true);

        assertThrows(RuntimeException.class,
            () -> accountService.createAccount(dto));

        verify(accountRepo, never()).save(any());
    }

    // ─── Test 2 ───────────────────────────────────────────
    @Test
    void shouldReturnFailedWhenCredentialsAreWrong() {
        AccountLoginDTO dto = new AccountLoginDTO();
        dto.setIdentifier("wrong@email.com");
        dto.setPassword("wrongpass");

        when(authenticationManager.authenticate(any()))
            .thenThrow(new BadCredentialsException("Bad credentials"));

        String result = accountService.verify(dto);

        assertEquals("Failed", result);
    }

    // ─── Test 3 ───────────────────────────────────────────
    @Test
    void shouldReturnAccountHolderNameWhenAccountExists() {
        Account account = new Account();
        account.setAccountNumber("100101250001");
        account.setAccountHolderName("Sarthak Thakor");

        when(accountRepo.findByAccountNumber("100101250001"))
            .thenReturn(Optional.of(account));

        var response = accountService.getAccountHolderName("100101250001");

        assertEquals(200, response.getStatusCode().value());
    }

    // ─── Test 4 ───────────────────────────────────────────
    @Test
    void shouldReturn404WhenAccountNotFound() {
        when(accountRepo.findByAccountNumber("NONEXISTENT"))
            .thenReturn(Optional.empty());

        var response = accountService.getAccountHolderName("NONEXISTENT");

        assertEquals(404, response.getStatusCode().value());
    }
}