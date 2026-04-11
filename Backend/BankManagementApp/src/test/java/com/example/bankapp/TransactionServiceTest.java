package com.example.bankapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.bankapp.DTO.TransferRequestDTO;
import com.example.bankapp.Exception.CustomValidationException;
import com.example.bankapp.entity.Account;
import com.example.bankapp.enums.AccountStatus;
import com.example.bankapp.implementation.TransactionServiceImp;
import com.example.bankapp.repository.AccountRepo;
import com.example.bankapp.repository.TransactionRepo;
import com.example.bankapp.services.JWTservices;
import com.example.bankapp.services.NotificationService;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private AccountRepo accountRepo;

    @Mock
    private TransactionRepo transactionRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private NotificationService notificationService;

    @Mock
    private JWTservices jwtServices;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private TransactionServiceImp transactionService;

    // ─── Helper so no need to create demo account again and again ───────────────────────────────────────────
    private Account buildAccount(String accNo, double balance, AccountStatus status) {
        Account account = new Account();
        account.setAccountNumber(accNo);
        account.setBalance(balance);
        account.setStatus(status);
        account.setPin("encodedPin");
        account.setAccountHolderName("Test User");
        return account;
    }

    // ─── Test 1 ───────────────────────────────────────────
    @Test
    void shouldFailTransferWhenInsufficientBalance() {
        Account sender = buildAccount("ACC001", 100.0, AccountStatus.ACTIVE);
        Account receiver = buildAccount("ACC002", 500.0, AccountStatus.ACTIVE);

        TransferRequestDTO request = new TransferRequestDTO();
        request.setToAccountNumber("ACC002");
        request.setAmount(9999.0);
        request.setPin("1234");

        when(accountRepo.findByAccountNumber("ACC001"))
            .thenReturn(Optional.of(sender));
        when(accountRepo.findByAccountNumber("ACC002"))
            .thenReturn(Optional.of(receiver));
        when(passwordEncoder.matches("1234", "encodedPin"))
            .thenReturn(true);

        ResponseEntity<?> response =
            transactionService.transferMoney("ACC001", request);

        assertEquals(400, response.getStatusCode().value());
        // Balance should be unchanged
        assertEquals(100.0, sender.getBalance());
    }

    // ─── Test 2 ───────────────────────────────────────────
    @Test
    void shouldFailWhenTransferringToSameAccount() {
        Account account = buildAccount("ACC001", 1000.0, AccountStatus.ACTIVE);

        TransferRequestDTO request = new TransferRequestDTO();
        request.setToAccountNumber("ACC001");
        request.setAmount(100.0);
        request.setPin("1234");

        when(accountRepo.findByAccountNumber("ACC001"))
            .thenReturn(Optional.of(account));

        assertThrows(CustomValidationException.class,
            () -> transactionService.transferMoney("ACC001", request));
    }

    // ─── Test 3 ───────────────────────────────────────────
    @Test
    void shouldFailWhenReceiverNotFound() {
        Account sender = buildAccount("ACC001", 1000.0, AccountStatus.ACTIVE);

        TransferRequestDTO request = new TransferRequestDTO();
        request.setToAccountNumber("NONEXISTENT");
        request.setAmount(100.0);
        request.setPin("1234");

        when(accountRepo.findByAccountNumber("ACC001"))
            .thenReturn(Optional.of(sender));
        when(accountRepo.findByAccountNumber("NONEXISTENT"))
            .thenReturn(Optional.empty());

        ResponseEntity<?> response =
            transactionService.transferMoney("ACC001", request);

        assertEquals(400, response.getStatusCode().value());
    }

    // ─── Test 4 ───────────────────────────────────────────
    @Test
    void shouldFailWhenPinIsWrong() {
        Account sender = buildAccount("ACC001", 1000.0, AccountStatus.ACTIVE);
        Account receiver = buildAccount("ACC002", 500.0, AccountStatus.ACTIVE);

        TransferRequestDTO request = new TransferRequestDTO();
        request.setToAccountNumber("ACC002");
        request.setAmount(100.0);
        request.setPin("wrongpin");

        when(accountRepo.findByAccountNumber("ACC001"))
            .thenReturn(Optional.of(sender));
        when(accountRepo.findByAccountNumber("ACC002"))
            .thenReturn(Optional.of(receiver));
        when(passwordEncoder.matches("wrongpin", "encodedPin"))
            .thenReturn(false);

        ResponseEntity<?> response =
            transactionService.transferMoney("ACC001", request);

        assertEquals(400, response.getStatusCode().value());
        // Balance must not change
        assertEquals(1000.0, sender.getBalance());
    }
}